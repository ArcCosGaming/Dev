package meta;

public class CPU_test {

	public static void main(String[] args) {
        int noOfProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println(noOfProcessors);
        for (int i = 0; i< noOfProcessors ; i++){
            new Thread(new Runnable() {

                @Override
                public void run() {
                    for(;;){
                    }
                }
            }).start();
        }
    }
}
