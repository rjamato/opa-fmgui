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
 *  File Name: FVViewInterface.java
 *
 *  Archive Source: $Source$
 *
 *  Archive Log:    $Log$
 *  Archive Log:    Revision 1.2  2015/01/11 21:24:07  jijunwan
 *  Archive Log:    generic table view with table model
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/04/12 19:46:33  fernande
 *  Archive Log:    Initial version
 *  Archive Log:
 *  Archive Log:    Revision 1.2  2014/04/04 19:31:30  rjtierne
 *  Archive Log:    Removed formatTable method from the interface. This method is now abstract and is implemented by the extending class.
 *  Archive Log:
 *  Archive Log:    Revision 1.1  2014/03/28 15:11:03  rjtierne
 *  Archive Log:    Initial Version
 *  Archive Log:
 *
 *  Overview: Public interface for a table class.
 *
 *  @author: rjtierne
 *
 ******************************************************************************/

package com.intel.stl.ui.common;

import javax.swing.table.TableModel;

public interface FVViewInterface<T extends TableModel> {

    /**
     * 
     * Description: Returns the index of the selected row in the view
     * 
     * @return selected row number
     */
    public int getSelectedRowNumber();

    /**
     * 
     * Description: Initializes the model in the view
     * 
     * @param pModel
     *            - model class to support the table view
     */
    public void setModel(T pModel);

} // interface FVViewInterface
