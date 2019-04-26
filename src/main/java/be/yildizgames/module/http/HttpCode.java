package be.yildizgames.module.http;

public class HttpCode {

    public static boolean isError(int code) {
        return code >= 400 && code < 600;
    }

    public static boolean isSuccessful(int code) {
        return code >= 200 && code < 300;
    }

    public static boolean isDirected(int code) {
        return code >= 300 && code < 400;
    }

}
