package com.nitindhar.kampr.data;

import com.nitindhar.kampr.models.Session;

public interface SessionDao {

    public boolean sessionExists();

    public boolean storeSession(Session session) throws Exception;

    public boolean removeSession();

    public boolean sessionWasEulaAccepted();

    public boolean sessionEulaAccepted();

}