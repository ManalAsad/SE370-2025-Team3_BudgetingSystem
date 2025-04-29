package org.project.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.HexFormat;
import java.util.Random;

import org.project.database.DatabaseConnection;

public class UserAuthenticationService
{
    public boolean createAccount(String username, String password)
    {
        boolean success;
        try {
            String salt, hash, sql;
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement;

            salt = generateSalt();
            hash = generateHash(salt + password);

            sql = "INSERT INTO users (username, hash, salt) VALUES (?, ?, ?)";

            statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, hash);
            statement.setString(3, salt);

            statement.executeUpdate();

            statement.close();
            connection.close();

            success = true;
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    public boolean authenticate(String username, String password)
    {
        boolean success;
        try {
            String sql;
            String salt = "";
            String hash = "";
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement;
            ResultSet resultSet;

            sql = "SELECT * FROM users WHERE username=?";

            statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            resultSet = statement.executeQuery();

            // checking if the query did not return any rows
            if (resultSet.isBeforeFirst()) {
                // there were rows returned, assuming username
                // is unique, so there should only be one row
                // returned in this case
                while (resultSet.next()) {
                    salt = resultSet.getString("salt");
                    hash = resultSet.getString("hash");
                }

                if (generateHash(salt + password).equals(hash)) {
                    // user entered the correct password
                    success = true;
                }
                else {
                    // password was invalid
                    success = false;
                }
            }
            else {
                // there were zero rows returned
                // username was invalid
                success = false;
            }

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception e) {
            success = false;
            e.printStackTrace();
        }

        return success;
    }

    // uses SHA-256 hashing function to generate
    // a hash given a prepended password as input
    public String generateHash(String prependedPassword) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest;
        byte[] hashBytes;

        messageDigest = MessageDigest.getInstance("SHA-256");
        hashBytes = messageDigest.digest(prependedPassword.getBytes());

        return HexFormat.of().formatHex(hashBytes);
    }

    // generates a random string (aka salt) that will
    // be prepended to a password entered by the user
    // this combined string is used as input to a hash function
    public String generateSalt()
    {
        String salt = "";
        Random random = new Random();

        for (int i = 0; i < 8; ++i) {
            if (random.nextInt(2) == 1) // using a random number between 0 and 1 to have random execution
                salt += random.nextInt(10); // generating random number between 0 and 9
            else
                salt += (char)(random.nextInt(26) + 97); // generating random character between a and z
        }

        return salt;
    }
}
