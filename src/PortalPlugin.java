import java.util.logging.Level;


public class PortalPlugin extends Plugin {
	static final PortalListener listener = new PortalListener();

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub

	}
	public void initialize()
	{
		listener.a.log(Level.INFO, "Loaded Portal Plugin! v1.0.05");
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener ,this,PluginListener.Priority.HIGH);
		//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener ,this,PluginListener.Priority.HIGH);
	}

}
