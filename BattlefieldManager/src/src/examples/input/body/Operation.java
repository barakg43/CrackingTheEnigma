package src.examples.input.body;

public enum Operation {

    PLUS {
        @Override
        public double eval(double x, double y) {
            return x + y;
        }

        @Override
        public String getSign() {
            return "+";
        }
    },
    MINUS {
        @Override
        public double eval(double x, double y) {
            return x - y;
        }

        @Override
        public String getSign() {
            return "-";
        }

    },
    TIMES {
        @Override
        public double eval(double x, double y) {
            return x * y;
        }

        @Override
        public String getSign() {
            return "*";
        }

    },
    DIVIDE {
        @Override
        public double eval(double x, double y) {
            return x / y;
        }
        @Override
        public String getSign() {
            return "/";
        }

    },
    POW {
        @Override
        public double eval(double x, double y) {
            return Math.pow(x, y);
        }

        @Override
        public String getSign() {
            return "^";
        }

    };

    public abstract double eval(double x, double y);

    public abstract String getSign();
}
