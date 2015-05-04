public class Exemple {
	
	private int result;
	private String nom;
	private int valeur;
	private Object object1 = new Object();
	private Object object2 = new Object();
	
	public Exemple(){}
	
	public void incVal() {valeur++;}
	public int getVal() {return valeur;}
	
	public int getResult(){
		return result;
	}
	
	public String getNom(){
		return nom;
	}
	
	public Object getObject1(){
		return object1;
	}
	
	public Object getObject2(){
		return object2;
	}
	
	public void factoriel(int n){
		int prov;
		prov = 1;
		for(int i = 1; i <= n; i++)
			prov = prov*i;
		result = prov;
	}
	
	public void min(int a, int b){
		if(a>b) 
			result = b;
		else
			result = a;
	}
	
	public void plusGrand(){
		nom = "Toto";
	}
	
	public void plusPetit(){
		nom = "Jean";
	}
	
	public Object renvoiNull(){
		return null;
	}
}