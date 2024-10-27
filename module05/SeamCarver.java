import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final int BORDER_ENERGY = 1000;
    private static final int BORDER_ENERGY_SQUARED = BORDER_ENERGY*BORDER_ENERGY;
    private Picture p;

    public SeamCarver(Picture picture) {
        // create a seam carver object based on the given picture
        nullGuard(picture);
        p = new Picture(picture);
    }

    public Picture picture() {
        // current picture
        return new Picture(p);
    }

    public int width() {
        // width of current picture
        return p.width();
    }

    public int height() {
        // height of current picture
        return p.height();
    }

    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        outOfBoundsGuard(x, y);
        int energySquared = energySquared(x, y);
        double energy = Math.sqrt(Double.valueOf(energySquared));
        return energy;
    }

    private int energySquared(int x, int y) {
        // energy squared of pixel at column x and row y
        outOfBoundsGuard(x, y);
        if (isAtBorder(x, y)) {
            return BORDER_ENERGY_SQUARED;
        }
        int energySquared = colorDiffSquared(p.get(x - 1, y), p.get(x + 1, y)) + colorDiffSquared(p.get(x, y - 1), p.get(x, y + 1));
        return energySquared;
    }

    private int colorDiffSquared(Color color, Color other) {
        return square(color.getRed() - other.getRed())
                + square(color.getGreen() - other.getGreen())
                + square(color.getBlue() - other.getBlue());
    }

    private int square(int value) {
        return value*value;
    }

    private boolean isAtBorder(int x, int y) {
        return (y == 0 || y == p.height() - 1) || (x == 0 || x == p.width() - 1);
    }

    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        return findHSeam();
    }

    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        return findVSeam();
    }

    private int[] findVSeam() {
        // finds vertical seam
        int height = p.height();
        int width = p.width();

        double[][] e = new double[height][width];

        for (int x = 0; x < width; x++) {
            e[0][x] = energy(x, 0);
        }

        for (int y = 1; y < height; y++) {
            int prevy = y - 1;
            for (int x = 0; x < width; x++) {
                e[y][x] = e[prevy][x];
                if (x > 0 && e[prevy][x - 1] < e[y][x]) {
                    e[y][x] = e[prevy][x - 1];
                }
                if (x + 1 < width && e[prevy][x + 1] < e[y][x]) {
                    e[y][x] = e[prevy][x + 1];
                }
                e[y][x] += energy(x, y);
            }
        }

        int[] seam = new int[height];
        int lastRow = height - 1;
        seam[lastRow] = width - 1;
        for (int x = 0; x < width; x++) {
            int seamx = seam[lastRow];
            if (e[lastRow][x] < e[lastRow][seamx]) {
                seam[lastRow] = x;
            }
        }

        for (int y = height - 2; y >= 0; y--) {
            int x = seam[y + 1];
            seam[y] = x;
            if (x > 0 && e[y][x - 1] < e[y][seam[y]]) {
                seam[y] = x - 1;
            }
            if (x + 1 < width && e[y][x + 1] < e[y][seam[y]]) {
                seam[y] = x + 1;
            }
        }
        return seam;
    }

    private int[] findHSeam() {
        // finds horizontal seam
        int height = p.height();
        int width = p.width();

        double[][] e = new double[height][width];

        for (int y = 0; y < height; y++) {
            e[y][0] = energySquared(0, y);
        }

        for (int x = 1; x < width; x++) {
            int prevx = x - 1;
            for (int y = 0; y < height; y++) {
                e[y][x] = e[y][prevx];
                if (y > 0 && e[y - 1][prevx] < e[y][x]) {
                    e[y][x] = e[y - 1][prevx];
                }
                if (y + 1 < height && e[y + 1][prevx] < e[y][x]) {
                    e[y][x] = e[y + 1][prevx];
                }
                e[y][x] += energy(x, y);
            }
        }

        int[] seam = new int[width];
        int lastCol = width - 1;
        seam[lastCol] = height - 1;
        for (int y = 0; y < height; y++) {
            if (e[y][lastCol] < e[seam[lastCol]][lastCol]) {
                seam[lastCol] = y;
            }
        }

        for (int x = width - 2; x >= 0; x--) {
            int y = seam[x + 1];
            seam[x] = y;
            if (y > 0 && e[y - 1][x] < e[seam[x]][x]) {
                seam[x] = y - 1;
            }
            if (y + 1 < height && e[y + 1][x] < e[seam[x]][x]) {
                seam[x] = y + 1;
            }
        }
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from current picture
        removeSeamGuard(seam, true);
        Picture newp = new Picture(p.width(), p.height() - 1);
        for (int x = 0; x < p.width(); x++) {
            int seamy = seam[x];
            for (int y = 0; y < seamy; y++) {
                newp.set(x, y, p.get(x, y));
            }
            for (int y = seamy + 1; y < p.height(); y++) {
                newp.set(x, y - 1, p.get(x, y));
            }
        }
        p = newp;
    }

    public void removeVerticalSeam(int[] seam) {
        // remove vertical seam from current picture
        removeSeamGuard(seam, false);
        Picture newp = new Picture(p.width() - 1, p.height());
        for (int y = 0; y < p.height(); y++) {
            int seamx = seam[y];
            for (int x = 0; x < seamx; x++) {
                newp.set(x, y, p.get(x, y));
            }
            for (int x = seamx + 1; x < p.width(); x++) {
                newp.set(x - 1, y, p.get(x, y));
            }
        }
        p = newp;
    }



    public static void main(String[] args) {
        //  unit testing (optional)
        String basePath = "/home/zlv/Downloads/week2/";
        //        Picture p1 = new Picture(basePath + "6x5.png");
        //        SeamCarver sc1 = new SeamCarver(p1);
        //        sc1.findVerticalSeam();
        //        sc1.findHorizontalSeam();
        //        printVerticalSeamEnergy(basePath + "chameleon.png");
        //        printVerticalSeamEnergy(basePath + "HJoceanSmall.png");

        Picture p3 = new Picture(basePath + "chameleon.png");
        SeamCarver sc3 = new SeamCarver(p3);
        int[] verticalSeam = sc3.findVerticalSeam();
        sc3.removeVerticalSeam(verticalSeam);
    }

    private static void printVerticalSeamEnergy(String path) {
        Picture p2 = new Picture(path);
        SeamCarver sc2 = new SeamCarver(p2);
        int[] seam = sc2.findVerticalSeam();
        double energy2 = 0;
        for (int y = 0; y < p2.height(); y++) {
            energy2 += sc2.energy(seam[y], y);
        }
        System.out.println(String.format("Energy: %fd", energy2));
    }

    private void nullGuard(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    private void outOfBoundsGuard(int x, int y) {
        if (!(0 <= x && x <= p.width() - 1 && 0 <= y && y <= p.height() - 1)) {
            throw new IllegalArgumentException("Coordinates out of image bounds");
        }
    }

    private void removeSeamGuard(int[] seam, boolean isHorizontal) {
        int imageLength = p.width();
        if (isHorizontal) {
            imageLength = p.height();
        }
        imageTooSmallGuard(imageLength);
        nullGuard(seam);
        seamLengthGuard(seam, isHorizontal);
        seamContentGuard(seam, imageLength);
    }

    private void imageTooSmallGuard(int imageLength) {
        if (imageLength <= 1) {
            throw new IllegalArgumentException("Image too small");
        }
    }

    private void seamLengthGuard(int[] seam, boolean isHorizontal) {
        int expectedLength = p.height();
        if (isHorizontal) {
            expectedLength = p.width();
        }
        if (seam.length != expectedLength) {
            throw new IllegalArgumentException("Seam length does not match image length");
        }
    }

    private void seamContentGuard(int[] seam, int imageLength) {
        if (seam.length < 1) {
            throw new IllegalArgumentException("Seam with zero length");
        }
        seamValueGuard(seam[0], imageLength);
        for (int i = 1; i < seam.length; i++) {
            int value = seam[i];
            seamValueGuard(value, imageLength);
            seamPairValuesGuard(value, seam[i - 1]);
        }
    }

    private void seamValueGuard(int value, int imageLength) {
        if (value < 0 || value >= imageLength) {
            throw new IllegalArgumentException("Seam out of image bounds");
        }
    }

    private void seamPairValuesGuard(int value, int previousValue) {
        int diff = value - previousValue;
        if (diff < -1 || diff > 1) {
            throw new IllegalArgumentException("Seam value cannot differ more than 1 from previous seam value");
        }
    }
}
