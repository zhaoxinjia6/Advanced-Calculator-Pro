import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * 高级计算器 - 核心逻辑类
 * 支持：基本运算、科学计算、统计、进制转换
 */
public class AdvancedCalculator {

    // ========== 计算状态 ==========
    private double currentValue;
    private double memoryValue;
    private String lastOperation;
    private ArrayList<HistoryEntry> history;
    private String currentMode; // "basic", "scientific", "statistics"
    private String angleMode;   // "deg", "rad"
    private String numberFormat; // "decimal", "binary", "octal", "hex"

    // ========== 统计模式数据 ==========
    private ArrayList<Double> dataSet;

    // ========== 内部类：历史记录条目 ==========
    public static class HistoryEntry {
        public String expression;
        public String result;
        public String timestamp;

        public HistoryEntry(String expr, String result) {
            this.expression = expr;
            this.result = result;
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            this.timestamp = sdf.format(new Date());
        }

        public String toString() {
            return timestamp + "  " + expression + " = " + result;
        }
    }

    // ========== 构造方法 ==========
    public AdvancedCalculator() {
        this.currentValue = 0.0;
        this.memoryValue = 0.0;
        this.lastOperation = "";
        this.history = new ArrayList<HistoryEntry>();
        this.currentMode = "basic";
        this.angleMode = "deg";
        this.numberFormat = "decimal";
        this.dataSet = new ArrayList<Double>();
    }

    // ========== Getter / Setter ==========
    public double getCurrentValue() { return currentValue; }
    public void setCurrentValue(double value) { this.currentValue = value; }
    public double getMemoryValue() { return memoryValue; }
    public void setMemoryValue(double value) { this.memoryValue = value; }
    public String getLastOperation() { return lastOperation; }
    public ArrayList<HistoryEntry> getHistory() { return history; }
    public void clearHistory() { history.clear(); }
    public String getCurrentMode() { return currentMode; }
    public void setCurrentMode(String mode) { this.currentMode = mode; }
    public String getAngleMode() { return angleMode; }
    public void setAngleMode(String mode) { this.angleMode = mode; }
    public String getNumberFormat() { return numberFormat; }
    public void setNumberFormat(String format) { this.numberFormat = format; }

    // ========== 存储功能（Memory） ==========

    /**
     * 清除记忆：MC
     */
    public void memoryClear() {
        this.memoryValue = 0.0;
    }

    /**
     * 读取记忆：MR
     */
    public void memoryRecall() {
        this.currentValue = this.memoryValue;
    }

    /**
     * 记忆加：当前值存入记忆（M+）
     */
    public void memoryAdd() {
        this.memoryValue += this.currentValue;
    }

    /**
     * 记忆减：从记忆中减去当前值（M-）
     */
    public void memorySubtract() {
        this.memoryValue -= this.currentValue;
    }

    /**
     * 记忆存储：M+ (同memoryAdd，保留兼容)
     */
    public void memoryStore() {
        this.memoryValue = this.currentValue;
    }

    // ========== 基本运算 ==========

    public void add(double operand) {
        double result = this.currentValue + operand;
        addHistory(this.currentValue + " + " + operand, result);
        this.currentValue = result;
        this.lastOperation = "+";
    }

    public void subtract(double operand) {
        double result = this.currentValue - operand;
        addHistory(this.currentValue + " - " + operand, result);
        this.currentValue = result;
        this.lastOperation = "-";
    }

    public void multiply(double operand) {
        double result = this.currentValue * operand;
        addHistory(this.currentValue + " * " + operand, result);
        this.currentValue = result;
        this.lastOperation = "*";
    }

    public void divide(double operand) {
        if (operand == 0) {
            throw new ArithmeticException("Divide by zero!");
        }
        double result = this.currentValue / operand;
        addHistory(this.currentValue + " / " + operand, result);
        this.currentValue = result;
        this.lastOperation = "/";
    }

    public void power(double exponent) {
        double result = Math.pow(this.currentValue, exponent);
        addHistory(this.currentValue + "^" + exponent, result);
        this.currentValue = result;
        this.lastOperation = "^";
    }

    public void modulus(double operand) {
        if (operand == 0) {
            throw new ArithmeticException("Modulo by zero!");
        }
        double result = this.currentValue % operand;
        addHistory(this.currentValue + " % " + operand, result);
        this.currentValue = result;
        this.lastOperation = "%";
    }

    // ========== 一元运算 ==========

    public void square() {
        double result = this.currentValue * this.currentValue;
        addHistory("sqr(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "sqr";
    }

    public void cube() {
        double result = this.currentValue * this.currentValue * this.currentValue;
        addHistory("cube(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "cube";
    }

    public void squareRoot() {
        if (this.currentValue < 0) {
            throw new IllegalArgumentException("Cannot sqrt negative!");
        }
        double result = Math.sqrt(this.currentValue);
        addHistory("sqrt(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "sqrt";
    }

    public void cubeRoot() {
        double result = Math.cbrt(this.currentValue);
        addHistory("cbrt(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "cbrt";
    }

    public void reciprocal() {
        if (this.currentValue == 0) {
            throw new ArithmeticException("Cannot reciprocal zero!");
        }
        double result = 1.0 / this.currentValue;
        addHistory("1/" + this.currentValue, result);
        this.currentValue = result;
        this.lastOperation = "recip";
    }

    public void factorial() {
        long val = (long) this.currentValue;
        if (this.currentValue < 0 || this.currentValue != val || val > 20) {
            throw new IllegalArgumentException("Factorial: 0-20 only!");
        }
        long result = 1;
        for (int i = 2; i <= val; i++) {
            result *= i;
        }
        addHistory(this.currentValue + "!", result);
        this.currentValue = result;
        this.lastOperation = "fact";
    }

    public void negate() {
        this.currentValue = -this.currentValue;
        this.lastOperation = "neg";
    }

    // ========== 三角函数 ==========

    public void sin() {
        double angle = this.angleMode.equals("deg") ? Math.toRadians(this.currentValue) : this.currentValue;
        double result = Math.sin(angle);
        addHistory("sin(" + this.currentValue + (this.angleMode.equals("deg") ? " deg" : " rad") + ")", result);
        this.currentValue = result;
        this.lastOperation = "sin";
    }

    public void cos() {
        double angle = this.angleMode.equals("deg") ? Math.toRadians(this.currentValue) : this.currentValue;
        double result = Math.cos(angle);
        addHistory("cos(" + this.currentValue + (this.angleMode.equals("deg") ? " deg" : " rad") + ")", result);
        this.currentValue = result;
        this.lastOperation = "cos";
    }

    public void tan() {
        double angle = this.angleMode.equals("deg") ? Math.toRadians(this.currentValue) : this.currentValue;
        if (Math.abs(Math.cos(angle)) < 1e-10) {
            throw new ArithmeticException("tan(90 deg) is undefined!");
        }
        double result = Math.tan(angle);
        addHistory("tan(" + this.currentValue + (this.angleMode.equals("deg") ? " deg" : " rad") + ")", result);
        this.currentValue = result;
        this.lastOperation = "tan";
    }

    public void asin() {
        if (this.currentValue < -1 || this.currentValue > 1) {
            throw new IllegalArgumentException("asin range: -1 to 1");
        }
        double result = Math.asin(this.currentValue);
        if (this.angleMode.equals("deg")) {
            result = Math.toDegrees(result);
        }
        addHistory("asin(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "asin";
    }

    public void acos() {
        if (this.currentValue < -1 || this.currentValue > 1) {
            throw new IllegalArgumentException("acos range: -1 to 1");
        }
        double result = Math.acos(this.currentValue);
        if (this.angleMode.equals("deg")) {
            result = Math.toDegrees(result);
        }
        addHistory("acos(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "acos";
    }

    public void atan() {
        double result = Math.atan(this.currentValue);
        if (this.angleMode.equals("deg")) {
            result = Math.toDegrees(result);
        }
        addHistory("atan(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "atan";
    }

    // ========== 对数函数 ==========

    public void log10() {
        if (this.currentValue <= 0) {
            throw new IllegalArgumentException("log requires > 0!");
        }
        double result = Math.log10(this.currentValue);
        addHistory("log(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "log";
    }

    public void ln() {
        if (this.currentValue <= 0) {
            throw new IllegalArgumentException("ln requires > 0!");
        }
        double result = Math.log(this.currentValue);
        addHistory("ln(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "ln";
    }

    public void log2() {
        if (this.currentValue <= 0) {
            throw new IllegalArgumentException("log2 requires > 0!");
        }
        double result = Math.log(this.currentValue) / Math.log(2);
        addHistory("log2(" + this.currentValue + ")", result);
        this.currentValue = result;
        this.lastOperation = "log2";
    }

    // ========== 常量 ==========

    public void inputPi() {
        this.currentValue = Math.PI;
        addHistory("pi", Math.PI);
        this.lastOperation = "pi";
    }

    public void inputE() {
        this.currentValue = Math.E;
        addHistory("e", Math.E);
        this.lastOperation = "e";
    }

    // ========== 统计功能 ==========

    public void addDataPoint(double value) {
        dataSet.add(value);
        addHistory("Add data " + value, value);
    }

    public void clearData() {
        dataSet.clear();
    }

    public int getDataCount() {
        return dataSet.size();
    }

    public double getDataSum() {
        double sum = 0;
        for (double d : dataSet) {
            sum += d;
        }
        return sum;
    }

    public double getDataMean() {
        if (dataSet.isEmpty()) {
            throw new IllegalStateException("No data!");
        }
        return getDataSum() / dataSet.size();
    }

    public double getDataVariance() {
        if (dataSet.size() < 2) {
            throw new IllegalStateException("Need at least 2 data points!");
        }
        double mean = getDataMean();
        double sum = 0;
        for (double d : dataSet) {
            sum += (d - mean) * (d - mean);
        }
        return sum / (dataSet.size() - 1);
    }

    public double getDataStdDev() {
        return Math.sqrt(getDataVariance());
    }

    public double getDataMin() {
        if (dataSet.isEmpty()) {
            throw new IllegalStateException("No data!");
        }
        double min = dataSet.get(0);
        for (double d : dataSet) {
            if (d < min) min = d;
        }
        return min;
    }

    public double getDataMax() {
        if (dataSet.isEmpty()) {
            throw new IllegalStateException("No data!");
        }
        double max = dataSet.get(0);
        for (double d : dataSet) {
            if (d > max) max = d;
        }
        return max;
    }

    public double getDataMedian() {
        if (dataSet.isEmpty()) {
            throw new IllegalStateException("No data!");
        }
        ArrayList<Double> sorted = new ArrayList<Double>(dataSet);
        java.util.Collections.sort(sorted);
        int n = sorted.size();
        if (n % 2 == 1) {
            return sorted.get(n / 2);
        } else {
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        }
    }

    public String getDataSummary() {
        if (dataSet.isEmpty()) {
            return "No data";
        }
        return String.format(
                "Count: %d\nSum: %.4f\nMean: %.4f\nVariance: %.4f\nStdDev: %.4f\nMin: %.4f\nMax: %.4f\nMedian: %.4f",
                getDataCount(), getDataSum(), getDataMean(), getDataVariance(),
                getDataStdDev(), getDataMin(), getDataMax(), getDataMedian()
        );
    }

    // ========== 进制转换 ==========

    public String toBinary(double value) {
        long val = (long) value;
        return Long.toBinaryString(val);
    }

    public String toOctal(double value) {
        long val = (long) value;
        return Long.toOctalString(val);
    }

    public String toHex(double value) {
        long val = (long) value;
        return Long.toHexString(val).toUpperCase();
    }

    public long fromBinary(String binary) {
        return Long.parseLong(binary, 2);
    }

    // ========== 单位转换 ==========

    // 温度转换
    public double celsiusToFahrenheit(double c) {
        return c * 9.0 / 5.0 + 32;
    }

    public double fahrenheitToCelsius(double f) {
        return (f - 32) * 5.0 / 9.0;
    }

    public double celsiusToKelvin(double c) {
        return c + 273.15;
    }

    public double kelvinToCelsius(double k) {
        return k - 273.15;
    }

    // 长度转换
    public double cmToInch(double cm) {
        return cm / 2.54;
    }

    public double inchToCm(double inch) {
        return inch * 2.54;
    }

    public double kmToMile(double km) {
        return km / 1.609344;
    }

    public double mileToKm(double mile) {
        return mile * 1.609344;
    }

    // ========== 历史记录 ==========

    private void addHistory(String expression, double result) {
        String resultStr;
        if (result == (long) result) {
            resultStr = String.valueOf((long) result);
        } else {
            resultStr = String.format("%.10f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        history.add(new HistoryEntry(expression, resultStr));
    }

    // ========== 清除 ==========

    public void clear() {
        this.currentValue = 0.0;
        this.lastOperation = "";
    }

    public void clearAll() {
        this.currentValue = 0.0;
        this.memoryValue = 0.0;
        this.lastOperation = "";
        this.history.clear();
        this.dataSet.clear();
    }

    // ========== 格式化显示 ==========

    public String getDisplayValue() {
        String formatted;
        if (Double.isNaN(this.currentValue) || Double.isInfinite(this.currentValue)) {
            return "Error";
        }

        // 如果是二进制/八进制/十六进制模式，且当前值是整数
        if (!numberFormat.equals("decimal") && this.currentValue == (long) this.currentValue) {
            long val = (long) this.currentValue;
            if (numberFormat.equals("binary")) {
                return "0b" + Long.toBinaryString(val);
            } else if (numberFormat.equals("octal")) {
                return "0o" + Long.toOctalString(val);
            } else if (numberFormat.equals("hex")) {
                return "0x" + Long.toHexString(val).toUpperCase();
            }
        }

        if (this.currentValue == (long) this.currentValue) {
            formatted = String.valueOf((long) this.currentValue);
        } else {
            formatted = String.format("%.10f", this.currentValue);
            formatted = formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
        return formatted;
    }
}