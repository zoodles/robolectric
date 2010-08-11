package dalvik.system;

public class SamplingProfiler {
    public static SamplingProfiler getInstance() {
        return new SamplingProfiler();
    }

    public void setEventThread(Thread eventThread) {
    }
}
