package chat.hola.com.app.MQtt;

/**
 * Created by moda on 30/06/17.
 */

/*******************************************************************************
 * Copyright (c) 1999, 2014 IBM Corp.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.php.
 */

import android.content.Context;

import chat.hola.com.app.MQtt.CustomMQtt.IMqttActionListener;
import chat.hola.com.app.MQtt.CustomMQtt.IMqttToken;


class ActionListener implements IMqttActionListener {

    /**
     * Actions that can be performed Asynchronously <strong>and</strong> associated with a
     * {@link ActionListener} object
     */
    enum Action {
        /**
         * Connect Action
         **/
        CONNECT,
        /**
         * Disconnect Action
         **/
        DISCONNECT,
        /**
         * Subscribe Action
         **/
        SUBSCRIBE,
        /**
         * Publish Action
         **/
        PUBLISH
    }

    /**
     * The {@link Action} that is associated with this instance of
     * <code>ActionListener</code>
     **/
    private Action action;
    /**
     * The arguments passed to be used for formatting strings
     **/
    private String[] additionalArgs;

    /**
     * {@link Context} for performing various operations
     **/
    private Context context;

    /**
     * Creates a generic action listener for actions performed form any activity
     *
     * @param context        The application context
     * @param action         The action that is being performed
     * @param additionalArgs Used for as arguments for string formating
     */
    public ActionListener(Context context, Action action,
                          String... additionalArgs) {
        this.context = context;
        this.action = action;

        this.additionalArgs = additionalArgs;
    }

    /**
     * The action associated with this listener has been successful.
     *
     * @param asyncActionToken This argument is not used
     */
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        switch (action) {
            case CONNECT:
                connect();
                break;
            case DISCONNECT:
                disconnect();
                break;
            case SUBSCRIBE:
                subscribe();
                break;
            case PUBLISH:
                publish();
                break;
        }

    }

    /**
     * A publish action has been successfully completed, update connection
     * object associated with the client this action belongs to, then notify the
     * user of success
     */
    private void publish() {


    }

    /**
     * A subscribe action has been successfully completed, update the connection
     * object associated with the client this action belongs to and then notify
     * the user of success
     */
    private void subscribe() {


    }

    /**
     * A disconnection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void disconnect() {


    }

    /**
     * A connection action has been successfully completed, update the
     * connection object associated with the client this action belongs to and
     * then notify the user of success.
     */
    private void connect() {


    }

    /**
     * The action associated with the object was a failure
     *
     * @param token     This argument is not used
     * @param exception The exception which indicates why the action failed
     */
    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        switch (action) {
            case CONNECT:
                connect(exception);
                break;
            case DISCONNECT:
                disconnect(exception);
                break;
            case SUBSCRIBE:
                subscribe(exception);
                break;
            case PUBLISH:
                publish(exception);
                break;
        }

    }

    /**
     * A publish action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void publish(Throwable exception) {


    }

    /**
     * A subscribe action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void subscribe(Throwable exception) {

    }

    /**
     * A disconnect action was unsuccessful, notify user and update client history
     *
     * @param exception This argument is not used
     */
    private void disconnect(Throwable exception) {


    }

    /**
     * A connect action was unsuccessful, notify the user and update client history
     *
     * @param exception This argument is not used
     */
    private void connect(Throwable exception) {


    }

}