package com.gpu;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

@SuppressWarnings("unchecked")
public class GpuPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(GpuPluginGles.class);
		RuneLite.main(args);
	}


}
