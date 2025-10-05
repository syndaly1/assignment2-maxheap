package util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CSVWriter implements Closeable, Flushable {
    private final PrintWriter out;

    public CSVWriter(String path) throws IOException {
        File f = new File(path);
        File parent = f.getParentFile();
        if (parent != null) parent.mkdirs();
        this.out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, false), StandardCharsets.UTF_8));
    }

    public void header(String... cols) { writeRow((Object[]) cols); }

    public void writeRow(Object... cols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) sb.append(',');
            String s = String.valueOf(cols[i]).replace("\"", "\"\"");
            boolean q = s.contains(",") || s.contains("\n") || s.contains("\"");
            if (q) sb.append('"').append(s).append('"'); else sb.append(s);
        }
        out.println(sb.toString());
    }

    @Override public void flush() { out.flush(); }
    @Override public void close() { out.close(); }
}
