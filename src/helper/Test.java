package helper;


public class Test {
	

	public static void main(String[] args) throws Exception {
		new Thread(new Runnable(){
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName());
			}
		}).start();
		System.out.println(Thread.currentThread().getName());
	}

}
