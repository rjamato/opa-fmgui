package com.intel.stl.fecdriver.messages.command;

import com.intel.stl.fecdriver.ICommand;
import com.intel.stl.fecdriver.impl.STLStatement;
import com.intel.stl.fecdriver.messages.adapter.RmppMad;
import com.intel.stl.fecdriver.messages.response.FVResponse;

/**
 * Abstract class for all FVCommands. Sets the FVResponse associated with its
 * FVCommand.
 * 
 * @see FVMessage
 * @since JDK 1.3
 * @author Jason Wiseman
 * @version 1.0
 * 
 */
public abstract class FVCommand<E, F> extends FVMessage implements
        ICommand<FVResponse<F>> {
    private InputArgument input;

    private STLStatement submittingStatement;

    /**
     * Unique FVResponse for this FVCommand.
     */
    private FVResponse<F> fvResponse;

    /*
     * (non-Javadoc)
     * 
     * @see com.vieo.fv.message.FVMessage#setMessageID(long)
     */
    @Override
    public void setMessageID(long messageID) {
        super.setMessageID(messageID);
        if (fvResponse != null) {
            fvResponse.setMessageID(messageID);
        }
    }

    /**
     * Sets the FVResponse associated with this FVCommand.
     * 
     * @param fvResponse
     *            the response associated with this command.
     */
    protected void setResponse(FVResponse<F> fvResponse) {
        fvResponse.setMessageID(getMessageID());
        this.fvResponse = fvResponse;
    }

    /**
     * Gets the FVResponse for this FVCommand.
     * 
     * @return the FVResponse associated with this FVCommand.
     */
    @Override
    public FVResponse<F> getResponse() {
        return fvResponse;
    }

    /**
     * @return the input
     */
    public InputArgument getInput() {
        return input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(InputArgument input) {
        this.input = input;
        fvResponse.setDescription(input.toString());
    }

    public void setSubmittingStatement(STLStatement statement) {
        this.submittingStatement = statement;
    }

    public STLStatement getSubmittingStatement() {
        return submittingStatement;
    }

    public RmppMad prepareMad() {
        throw new UnsupportedOperationException();
    }

}
