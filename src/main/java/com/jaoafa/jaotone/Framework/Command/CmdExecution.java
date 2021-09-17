package com.jaoafa.jaotone.Framework.Command;

import java.util.ArrayList;

public record CmdExecution(CmdRouter.CmdRoutingData routingData, ArrayList<CmdOptionIndex> options) {
}
