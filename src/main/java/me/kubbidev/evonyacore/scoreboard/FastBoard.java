package me.kubbidev.evonyacore.scoreboard;

import me.kubbidev.evonyacore.players.EPlayer;
import me.kubbidev.evonyacore.players.Role;
import me.kubbidev.evonyacore.players.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class FastBoard {

    private final String[] SCOREBOARD_LINES = new String[] {
                    ChatColor.UNDERLINE + "" + ChatColor.RESET, ChatColor.ITALIC + "" + ChatColor.RESET, ChatColor.BOLD + "" + ChatColor.RESET, ChatColor.RESET + "" + ChatColor.RESET, ChatColor.GREEN + "" + ChatColor.RESET, ChatColor.DARK_GRAY + "" + ChatColor.RESET, ChatColor.GOLD + "" + ChatColor.RESET, ChatColor.RED + "" + ChatColor.RESET, ChatColor.YELLOW + "" + ChatColor.RESET, ChatColor.WHITE + "" + ChatColor.RESET,
                    ChatColor.DARK_GREEN + "" + ChatColor.RESET, ChatColor.BLUE + "" + ChatColor.RESET, ChatColor.STRIKETHROUGH + "" + ChatColor.RESET, ChatColor.MAGIC + "" + ChatColor.RESET, ChatColor.DARK_RED + "" + ChatColor.RESET
            };

    private final EPlayer player;
    private final Scoreboard scoreboard;

    private final List<String> lines = new ArrayList<>();
    private final List<Team> ranks = new ArrayList<>();
    private final List<Team> roles = new ArrayList<>();

    private Objective objective;
    private String title = ChatColor.RESET.toString();

    public FastBoard(EPlayer player) {
        this.player = Objects.requireNonNull(player, "player");
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for (int i = 0; i < 15; i++)
            scoreboard.registerNewTeam(SCOREBOARD_LINES[i]).addEntry(SCOREBOARD_LINES[i]);
        this.registerTeams();

        player.setScoreboard(this.scoreboard);
    }

    public void updateTitle(String title) {
        if (this.title.equals(Objects.requireNonNull(title, "title")))
            return;

        this.title = title;
        this.objective.setDisplayName(this.title);
    }

    public synchronized void updateLines(Collection<String> lines) {
        Objects.requireNonNull(lines, "lines");

        this.lines.clear();
        this.lines.addAll(lines);

        int i = 0;
        for (String line : this.lines) {
            String first = "";
            String second = "";
            if (!line.isEmpty()) {
                if (line.length() <= 16) {
                    first = line;
                }
                else {
                    int split = 16;
                    first = line.substring(0, split);
                    boolean copyColor = true;
                    if (first.endsWith(String.valueOf(Color.COLOR_CHAR))) {
                        copyColor = false;
                        split = 15;
                        first = line.substring(0, split);
                        if (first.substring(0, 14).endsWith(String.valueOf(Color.COLOR_CHAR))) {
                            split = 13;
                            first = line.substring(0, split);
                        }
                    }
                    if (copyColor)
                        second = ChatColor.getLastColors(first);
                    second = second + line.substring(split);
                }
            }
            final Team lineTeam = this.scoreboard.getTeam(SCOREBOARD_LINES[i]);
            if (second.length() > 16) {
                second = second.substring(0, 16);
            }

            if (!lineTeam.getPrefix().equals(first))
                lineTeam.setPrefix(first);
            if (!lineTeam.getSuffix().equals(second))
                lineTeam.setSuffix(second);
            i++;
        }
    }

    public EPlayer getPlayer() {
        return this.player;
    }

    public List<Team> getRanks() {
        return ranks;
    }

    public List<Team> getRoles() {
        return roles;
    }

    public void removeRoles() {
        this.roles.forEach(team -> scoreboard.getTeam(team.getName()).unregister());
        this.roles.clear();
    }

    public Team getTeam(String name) {
        return this.scoreboard.getTeam(name);
    }

    public void resetObjective() {
        if (this.objective != null)
            this.objective.unregister();

        this.objective = scoreboard.registerNewObjective(this.title, "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(this.title);

        IntStream.range(0, this.lines.size()).forEach(i -> this.objective.getScore(SCOREBOARD_LINES[i]).setScore(i));
        Bukkit.getOnlinePlayers().forEach(p -> {

            final String name  = p.getName();
            this.ranks.forEach(team -> {
                if (team.hasEntry(name))
                    team.removeEntry(name);
            });
        });
    }

    public void delete() {
        removeRoles();
        objective.unregister();
    }

    private void registerTeams() {
        for (Rank rank : Rank.values()) {
            final Team team = this.scoreboard.registerNewTeam(String.valueOf(rank.getValue()));
            team.setPrefix(rank.getScoreBoardPrefix() + rank.getColor());

            this.ranks.add(team);
        }
        for (Role role : Role.values()) {
            final String index = String.valueOf(role.isDemon() ? 0 : 1);
            final Team team = this.scoreboard.registerNewTeam(index + role.getName());
            team.setPrefix(role.getPrefix() + " " + (role.isDemon() ? ChatColor.RED : ChatColor.GREEN));

            this.roles.add(team);
        }
        this.scoreboard.registerNewTeam("player");
        this.scoreboard.registerNewTeam("spectator").setPrefix(ChatColor.DARK_GRAY.toString());
    }
}
