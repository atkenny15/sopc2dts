/*
sopc2dts - Devicetree generation for Altera systems

Copyright (C) 2011 - 2012 Walter Goossens <waltergoossens@home.nl>

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package sopc2dts.lib.components.altera;

import sopc2dts.Logger;
import sopc2dts.Logger.LogLevel;
import sopc2dts.lib.AvalonSystem;
import sopc2dts.lib.components.Interface;
import sopc2dts.lib.components.SopcComponentDescription;
import sopc2dts.lib.components.BasicComponent;
import sopc2dts.lib.components.base.SICEthernet;

public class SICTrippleSpeedEthernet extends SICEthernet {

	private boolean removed = false;

	public SICTrippleSpeedEthernet(String cName, String iName, String ver, SopcComponentDescription scd) {
		super(cName, iName, ver, scd);
	}

	protected BasicComponent getDMAEngineForIntf(Interface intf)
	{
		if((intf!=null) && (intf.getConnections().size()>0))
		{
			BasicComponent comp = null;
			if(intf.isMaster())
			{
				comp = intf.getConnections().firstElement().getSlaveModule();
			} else {
				comp = intf.getConnections().firstElement().getMasterModule();
			}
			return comp;
		} else if (intf!=null)
		{
			Logger.logln("Interface " + intf.getName() + " has no connections.", LogLevel.INFO);
		}
		return null;
	}

	@Override
	public boolean removeFromSystemIfPossible(AvalonSystem sys)
	{
		if (!removed) {
			removed = true;
			String phyModeString = getParamValByName("ifGMII");
			String phyModeStringsgmii = getParamValByName("enable_sgmii");
			setPhyMode(PhyMode.RGMII);
			if(phyModeStringsgmii.equals("true"))
			{
				Logger.logln("enable_sgmii");	
			        setPhyMode(PhyMode.SGMII);
			} else {
				if(phyModeString.equals("MII"))
				{
					setPhyMode(PhyMode.MII);
				} else if(phyModeString.equals("MII_GMII"))
				{
					setPhyMode(PhyMode.GMII);
				} else if(phyModeString.equals("RGMII"))
				{
					setPhyMode(PhyMode.RGMII);
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
