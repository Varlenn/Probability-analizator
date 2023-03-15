package example.plot;

public class Data {
    private Character asciiChar;
    private Integer asciiNum;
    private Integer num;
    private String probability;

    public Data(Character asciiChar, Integer asciiNum, Integer num, String probability) {
        this.asciiChar = asciiChar;
        this.asciiNum = asciiNum;
        this.num = num;
        this.probability = probability;
    }

    public Character getAsciiChar() {
        return asciiChar;
    }

    public void setAsciiChar(Character asciiChar) {
        this.asciiChar = asciiChar;
    }

    public Integer getAsciiNum() {
        return asciiNum;
    }

    public void setAsciiNum(Integer asciiNum) {
        this.asciiNum = asciiNum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
