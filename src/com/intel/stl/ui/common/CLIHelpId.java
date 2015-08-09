/**
 * INTEL CONFIDENTIAL
 * Copyright (c) 2014 Intel Corporation All Rights Reserved.
 * The source code contained or described herein and all documents related to the source code ("Material")
 * are owned by Intel Corporation or its suppliers or licensors. Title to the Material remains with Intel
 * Corporation or its suppliers and licensors. The Material contains trade secrets and proprietary and
 * confidential information of Intel or its suppliers and licensors. The Material is protected by
 * worldwide copyright and trade secret laws and treaty provisions. No part of the Material may be used,
 * copied, reproduced, modified, published, uploaded, posted, transmitted, distributed, or disclosed in
 * any way without Intel's prior express written permission. No license under any patent, copyright,
 * trade secret or other intellectual property right is granted to or conferred upon you by disclosure
 * or delivery of the Materials, either expressly, by implication, inducement, estoppel or otherwise.
 * Any license under such intellectual property rights must be express and approved by Intel in writing.
 */

/*******************************************************************************
 *                       I N T E L   C O R P O R A T I O N
 *	
 *  Functional Group: Fabric Viewer Application
 *
 *  File Name: HelpId.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.3  2014/12/10 20:59:42  jijunwan
 *  Archive Log:    fixed a parser issue
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/12/04 23:19:07  jijunwan
 *  Archive Log:    added hashcode just in case we may use it in a set in the future
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/12/04 23:17:39  jijunwan
 *  Archive Log:    only list cmds in help list
 *  Archive Log:
 *
 *  Overview: 
 *
 *  @author: jijunwan
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

public class CLIHelpId {
    public static final String DELIMITER = "#";

    private final String cmd;

    private final String arg;

    /**
     * Description:
     * 
     * @param cmd
     * @param arg
     */
    public CLIHelpId(String cmd, String arg) {
        super();
        this.cmd = cmd;
        this.arg = arg;
    }

    /**
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * @return the arg
     */
    public String getArg() {
        return arg;
    }

    public String idString() {
        return cmd + DELIMITER + arg;
    }

    public static CLIHelpId valueOf(String str) {
        String cmd = str;
        String arg = null;

        int pos = str.indexOf(DELIMITER);
        if (pos > 0) {
            cmd = str.substring(0, pos);
            arg = str.substring(pos);
        } else if (pos == 0) {
            throw new IllegalArgumentException(
                    "Command name start with illegal char");
        }
        return new CLIHelpId(cmd, arg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((arg == null) ? 0 : arg.hashCode());
        result = prime * result + ((cmd == null) ? 0 : cmd.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CLIHelpId other = (CLIHelpId) obj;
        if (arg == null) {
            if (other.arg != null) {
                return false;
            }
        } else if (!arg.equals(other.arg)) {
            return false;
        }
        if (cmd == null) {
            if (other.cmd != null) {
                return false;
            }
        } else if (!cmd.equals(other.cmd)) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CLIHelpId [cmd=" + cmd + ", arg=" + arg + "]";
    }

}
