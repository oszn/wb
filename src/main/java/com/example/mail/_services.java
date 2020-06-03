package com.example.mail;

import java.util.List;

public interface _services {
    public boolean sendEmail(String _url, String _payload);

    public boolean sendEmailBatch(String _url, String _payload);

    public boolean validateEmailAddress(String _url);

}