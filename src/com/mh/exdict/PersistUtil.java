package com.mh.exdict;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PersistUtil {

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private PersistUtil() {}

    private static boolean doLog = true;

    public static void setDoLog(boolean doLog) {
        PersistUtil.doLog = doLog;
    }

    public static void log(String title, Object message) {
        System.out.println(title + ": " + message);
    }

    public synchronized static void save(Dict dict, OutputStream out) throws IOException {
        String json = gson.toJson(dict, Dict.class);
        log("SaveDict", json);
        out.write(json.getBytes("utf-8"));
    }

    public synchronized static Dict load(InputStream in) throws IOException {
        Dict dict = gson.fromJson(new InputStreamReader(in, "utf-8"), Dict.class);
        log("LoadDict", dict);
        return dict;
    }

    public static void ensureDirExist(File dir) throws IOException {
        if (dir != null || !dir.exists()) {
            dir.mkdirs();
        }
    }
}
