import java.util.Comparator;

/**
 * GhostComparator is a custom comparator used to sort Ghost objects (which are represented as Block objects)
 * based on their X position on the game board.
 */
class GhostComparator implements Comparator<PacMan.Block> {

    /**
     * Compares two Block objects (representing Ghosts) based on their X-coordinate.
     *
     * @param b1 The first Block to be compared.
     * @param b2 The second Block to be compared.
     * @return A negative integer, zero, or a positive integer as the first Block's X-coordinate
     *         is less than, equal to, or greater than the second Block's X-coordinate.
     */
    @Override
    public int compare(PacMan.Block b1, PacMan.Block b2) {
        // Compare the X coordinates of the two Blocks (Ghosts)
        return Integer.compare(b1.getX(), b2.getX());
    }
}
