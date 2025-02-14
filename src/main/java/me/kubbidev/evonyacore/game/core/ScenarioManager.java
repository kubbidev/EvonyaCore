package me.kubbidev.evonyacore.game.core;

import com.google.common.base.Preconditions;
import me.kubbidev.evonyacore.EvonyaPlugin;
import me.kubbidev.evonyacore.game.core.scenario.Option;
import me.kubbidev.evonyacore.game.core.scenario.Scenario;
import me.kubbidev.evonyacore.game.core.scenario.ScenarioListener;
import me.kubbidev.evonyacore.game.core.scenario.Settings;
import me.kubbidev.evonyacore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class ScenarioManager {

    private final GameInstance gameInstance;

    private final List<Scenario> registeredScenarios;
    private final Map<Scenario, ScenarioListener> enabledScenarios;

    public ScenarioManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.registeredScenarios = new ArrayList<>();
        this.enabledScenarios = new HashMap<>();
    }

    public boolean isRegistered(Scenario scenario) {
        return this.registeredScenarios.contains(scenario);
    }

    public void registerScenario(Scenario scenario) {
        this.registeredScenarios.add(scenario);
    }

    public void unRegisterScenario(Scenario scenario) {
        this.registeredScenarios.remove(scenario);
    }

    public void disableScenario(Scenario scenario) {
        Preconditions.checkState(isRegistered(scenario), "The specified scenario is not registered!");

        final ScenarioListener scenarioListener = this.enabledScenarios.get(scenario);
        this.enabledScenarios.remove(scenario);

        if (scenarioListener != null) {
            HandlerList.unregisterAll(scenarioListener);
            scenarioListener.onDisable();
        }
    }

    public void enableScenario(Scenario scenario) {
        Preconditions.checkState(isRegistered(scenario), "The specified scenario is not registered!");
        if (isEnabled(scenario))
            return;
        final Class<? extends ScenarioListener> listenerClass = scenario.getListener();
        try {
            final ScenarioListener scenarioListener = listenerClass.getDeclaredConstructor(GameInstance.class).newInstance(gameInstance);

            this.enabledScenarios.put(scenario, scenarioListener);
            this.loadScenarioOptions(scenarioListener);

            scenarioListener.onEnable();
            if (isEnabled(scenario))
                Bukkit.getServer().getPluginManager().registerEvents(scenarioListener, EvonyaPlugin.INSTANCE);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEnabled(Scenario scenario) {
        return this.enabledScenarios.containsKey(scenario);
    }

    public List<Scenario> getRegisteredScenarios() {
        return registeredScenarios;
    }

    public synchronized Set<Scenario> getEnabledScenarios() {
        return this.enabledScenarios.keySet();
    }

    public ScenarioListener getScenarioListener(Scenario scenario) {
        return this.enabledScenarios.get(scenario);
    }

    public void disableAllScenarios() {
        final Set<Scenario> active = new HashSet<>(getEnabledScenarios());
        for (Scenario scenario : active)
            disableScenario(scenario);
    }

    public void registerAllScenarios() {
        for (Scenario scenario : this.registeredScenarios) {
            enableScenario(scenario);
        }
    }

    private void loadScenarioOptions(ScenarioListener listener) throws IllegalAccessException {
        final List<Field> optionFields = getAnnotatedFields(listener.getClass(), Option.class);
        if (optionFields.isEmpty())
            return;
        for (Field field : optionFields) {
            final Option option = field.getAnnotation(Option.class);
            final Settings settings = option.key();
            final Object value = settings.getValue();

            field.set(listener, value);
        }
    }

    public static List<Field> getAnnotatedFields(Class<?> c, Class<? extends Annotation> annotation) {
        List<Field> fields = new ArrayList<>();
        for (Field field : c.getFields()) {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        return fields;
    }

    public static boolean isCancelled(Cancellable event, Cancellable subEvent) {
        if (subEvent.isCancelled()) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    public Optional<Scenario> getScenarioByKey(String key) {
        return Arrays.stream(Scenario.values()).filter(s -> s.getKey().equals(key)).findFirst();
    }
}
