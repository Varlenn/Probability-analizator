package example.plot;


import java.io.UnsupportedEncodingException;
import java.util.List;

public interface Generator {
    int setRandom(int min, int max);
    List<Integer> generate(int symbols) throws UnsupportedEncodingException;
}
