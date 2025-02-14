package me.kubbidev.evonyacore.menu;

import me.kubbidev.evonyacore.EvonyaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public abstract class PaginatedMenu extends Menu {

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility, EvonyaPlugin plugin) {
        super(playerMenuUtility, plugin);
    }
    protected int page = 0;
    protected int index = 0;

    protected int getMaxItemsPerPage() {
        if (getBorder() == MenuBorder.CORNER)
            return getMaxItemsCornerMenu();
        return getMaxItemsLinesMenu();
    }
    private int getMaxItemsCornerMenu() {
        final int rows = getRows();
        switch (rows) {
            case 3:
                return 7;
            case 4:
                return 14;
            case 5:
                return 21;
            case 6:
                return 28;
        }
        throw new RuntimeException("Cannot defined max items per pages with " + rows + " rows");
    }
    private int getMaxItemsLinesMenu() {
        final int rows = getRows();
        switch (rows) {
            case 3:
            case 4:
                return ((rows * 9) - 9);
            case 5:
            case 6:
                return ((rows * 9) - 27);
        }
        throw new RuntimeException("Cannot defined max items per pages with " + rows + " rows");
    }

    protected int getIndex() {
        final List<Integer> ints;
        if (getBorder() == MenuBorder.CORNER)
            ints = getIndexCornerMenu();
        else ints = getIndexLineMenu();
        return ints.get(index % ints.size());
    }
    private List<Integer> getIndexCornerMenu() {
        final int rows = getRows();
        final List<Integer> index = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14, 15, 16));
        switch (rows) {
            case 4:
                IntStream.rangeClosed(19, 25).forEach(index::add);
                break;
            case 5:
                IntStream.rangeClosed(19, 25).forEach(index::add);
                IntStream.rangeClosed(28, 34).forEach(index::add);
                break;
            case 6:
                index.clear();
                IntStream.rangeClosed(20, 24).forEach(index::add);
                IntStream.rangeClosed(29, 33).forEach(index::add);
                break;
        }
        return index;
    }
    private List<Integer> getIndexLineMenu() {
        final int rows = getRows();
        final List<Integer> index = new ArrayList<>();
        switch (rows) {
            case 3:
            case 4:
                 IntStream.rangeClosed(9, 17).forEach(index::add);
                break;
            case 5:
            case 6:
                IntStream.range(18, ((rows * 9) - 9)).forEach(index::add);
                break;
            default:
                throw new RuntimeException("Cannot defined index with " + rows + " rows");

        }
        return index;
    }
}
