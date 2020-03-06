package de.fload.commands;

import de.fload.core.Settings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static de.fload.database.DatabaseAction.isAllowed;
import static de.fload.util.FUNCTION.*;

public class cmdWeather implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(!isAllowed(event, "weather")) {
            printWarning(event, "You are not allowed to use that command");
            return;
        }

        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + args[0].trim() + "&units=metric&appid=" + Settings.getInstance().getOwaAPIKey());
            String data = new Scanner( url.openStream() ).useDelimiter( "\\Z" ).next();
            JSONObject obj = (JSONObject) new JSONParser().parse(data);

            String temp = ((JSONObject) obj.get("main")).get("temp").toString();

            JSONArray weather = (JSONArray) obj.get("weather");
            JSONObject nil = (JSONObject) weather.get(0);
            String main = nil.get("main").toString();

            event.getTextChannel().sendMessage("In " + args[0] + " there are currently " + main.toLowerCase() + " at " + temp + "°C.").queue();

        } catch (IOException | ParseException e) {
            outputException(e);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        cmdMsg(event, "weather");
    }

    @Override
    public String help() {
        return "Weather at your Location";
    }
}
