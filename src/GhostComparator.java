import java.util.Comparator;

class GhostComparator implements Comparator<PacMan.Block> {
    @Override
    public int compare(PacMan.Block b1, PacMan.Block b2) {
        return Integer.compare(b1.getX(), b2.getX());
    }
}
