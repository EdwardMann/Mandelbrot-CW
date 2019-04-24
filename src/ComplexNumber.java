
public class ComplexNumber {

	
	private double real;
	private double imaginary;
	
	
	public ComplexNumber (double realNumber, double imaginaryNumber){
		this.real = realNumber;
		this.imaginary = imaginaryNumber;
	}
	
    public double getReal(){
    	return real;
    }
    
    public double getImaginary(){
    	return imaginary;
    }
    
    public void setReal(double real){
    	this.real = real;
    }
    
    public void setImaginary(double imaginary){
    	this.imaginary = imaginary;
    }
	
    
	public ComplexNumber square(){
		double zx = real * real;
		double xy = 2 * real * imaginary;
		double zy = imaginary * imaginary;
		
		real=zx - zy;
		imaginary=xy;
		return new ComplexNumber (real,imaginary);
	} 
	  public ComplexNumber Times (ComplexNumber z) {

		    return new ComplexNumber(real*z.real - imaginary*z.imaginary, real*z.imaginary + imaginary*z.real);
		    
		  }
	
	public double absReal(){
		return Math.sqrt(real * real);
	}

	public double absIma(){
		return Math.sqrt(imaginary * imaginary);
	}
	public double modulusSquared(){
		
		double a = absReal() * absReal();
		double b = absIma() * absIma();
		return a + b;
	}
	
	public ComplexNumber complexSquared(){
		real = absReal();
		imaginary = absIma();
		double zx = real * real;
		double xy = 2 * real * imaginary;
		double zy = imaginary * imaginary;

		
		real=zx - zy;
		imaginary=xy;
		return new ComplexNumber(real, imaginary);
	}
	
	/*return a new Complex object whose value is the sum of 
	* the complex number d and (this) complex number */
    public ComplexNumber add(ComplexNumber d) {
    	real += d.getReal();
    	imaginary += d.getImaginary();
    	
    	return new ComplexNumber(real, imaginary);
    }

	
    public String toString() {
        if (imaginary == 0) return real + "";
        if (real == 0) return imaginary + "i";
        if (imaginary <  0) return real + " - " + (-imaginary) + "i";
        return real + " + " + imaginary + "i";
    }
    

}