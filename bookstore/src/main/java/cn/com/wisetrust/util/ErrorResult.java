package cn.com.wisetrust.util;
public class ErrorResult {

    public int res;
    public String err;

    public ErrorResult(int result, String error) {
        this.res = result;
        this.err = error;
    }

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public String getErr() {
		return err;
	}

	public void setErr(String err) {
		this.err = err;
	}
    
    
    
}