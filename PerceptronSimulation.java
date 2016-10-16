/**
 * The PerceptronSimulation program simulates a perceptron.  It takes in a
 * threshold, learning rate (alpha), the initial weights, and an operation.
 * The program will output a chart that follows through each epoch and arrive
 * at finalized weights.  This program contains no error checking, so running
 * this program against the XOR operation will result in an infinite loop.
 *
 * @author Zack Zatkin-Gold
 */
import java.util.Arrays;

public class PerceptronSimulation {
    private static double t;
    private static double alpha;
    private static double w1;
    private static double w2;
    private static int threshold;
    private static Operation op;

    PerceptronSimulation() {
        int epoch = 1;
        System.out.println("Epoch x1 x2 expected actual error   w1   w2");
        boolean error = true;
        while(error && epoch <= threshold) {
            error = false;
            for(int i = 0; i < 4; i++) {
                int x1 = i >> 1;
                int x2 = i & 1;

                int expected = op.expected(x1,x2);
                int actual = (w1 * x1 + w2 * x2) > t ? 1 : 0;
                int e = expected - actual;
                if(e != 0) {
                    w1 = w1 + (alpha * x1 * e);
                    w2 = w2 + (alpha * x2 * e);
                    error = true;
                }
                System.out.printf("%5d %2d %2d %8d %6d %5d %4.1f %4.1f%n",
                                epoch, x1, x2, expected, actual, e, w1, w2);
            }
            epoch += 1;
        }
    }

    public static void main(String[] args) {
        if(args.length < 5 || args.length > 6) usage();
        t = Double.parseDouble(args[0]);
        alpha = Double.parseDouble(args[1]);
        w1 = Double.parseDouble(args[2]);
        w2 = Double.parseDouble(args[3]);
        threshold = Integer.parseInt(args[4]);
        op = Operation.NAND;
        if(args.length == 6) {
            switch(args[5]) {
                case "AND":
                    op = Operation.AND;
                break;
                case "OR":
                    op = Operation.OR;
                break;
                case "XOR":
                    op = Operation.XOR;
                break;
                case "NAND":
                    op = Operation.NAND;
                break;
            }
        }

        new PerceptronSimulation();
    }

    public static void usage() {
        System.err.println("Usage: java PerceptronSimulation <t> <alpha> <w1> "
                                                          + "<w2> <op>");
        System.err.println("<t> = Perceptron threshold");
        System.err.println("<alpha> = Perceptron learning rate");
        System.err.println("<w1> = Weight 1");
        System.err.println("<w2> = Weight 2");
        System.err.println("<et> = Epoch threshold (i.e. maximum epochs to "
                                                      + "attempt learning)");
        System.err.println("<op> = Perceptron operation ("
            + Arrays.toString(Operation.values()).replaceAll("\\]|\\[","")+")");
        System.exit(1);
    }

    /**
     * The Operation enum represents an operation that's available to the user.
     * XOR is exclusive-or, OR is logical or, AND is logical and, and NAND is
     * Sheffer stroke.
     */
    private static enum Operation {
        AND("AND"), OR("OR"), XOR("XOR"), NAND("NAND");

        final String op;

        Operation(String op) {
            this.op = op;
        }

        public int expected(int x1, int x2) {
            switch(op) {
                case "AND":
                    return x1 & x2;
                case "OR":
                    return x1 | x2;
                case "XOR":
                    return x1 ^ x2;
                case "NAND":
                default:
                    return ~(x1 & x2);
            }
        }
    }
}
