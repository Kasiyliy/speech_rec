package kz.halyq.speech_rec.utils;

public class MessageUtils {

    public static String getMessageJSON(String body){
        return String.format("{ \"message\" : \"%1$s\"}" , body);
    }

}
