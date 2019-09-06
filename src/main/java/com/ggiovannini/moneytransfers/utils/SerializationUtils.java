package com.ggiovannini.moneytransfers.utils;

import com.ggiovannini.moneytransfers.model.Account;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializer;

import java.math.BigDecimal;

public class SerializationUtils {

    public static JsonSerializer<Account> buildAccountSerializer() {
        JsonSerializer<Account> serializer = (src, typeOfSrc, context) -> {
            JsonObject jsonAccount = new JsonObject();

            jsonAccount.addProperty("id", src.getId());
            jsonAccount.addProperty("balance", src.getBalance().getAmount());

            return jsonAccount;
        };

        return serializer;
    }

    public static JsonDeserializer<Account> buildAccountDeserializer() {
        JsonDeserializer<Account> deserializer = (json, typeOfT, context) -> {
            String id = json.getAsJsonObject().get("id").getAsString();
            BigDecimal balance = json.getAsJsonObject().get("balance").getAsBigDecimal();


            Account account = new Account(id, balance);
            return account;
        };

        return deserializer;
    }
}
