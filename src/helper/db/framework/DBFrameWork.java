package helper.db.framework;

public interface DBFrameWork {
	public boolean query(String sql, boolean ShowResult);
	public void shutdown();
	public void update(String sql);

}
