package utilidades;

public class Hilo implements Runnable{
	
	Thread hilo;
	int segundos;
	int minutos, horas;
	Archivos arch;

	@Override
	public void run() {
		System.out.println("Hilo corriendo");
		while(segundos >= 0) {
			try {
				Thread.sleep(100);
				//System.out.println("Segundos pasados: "+segundos);
				segundos++;
				
				if(segundos%60 == 0) {
					segundos = 0;
					minutos++;
					Recursos.minutosJugados ++;
					//System.out.println("Minutos pasados: "+Recursos.minutosJugados);
					
					if(minutos%60 == 0) {
						horas++;
						Recursos.horasJugadas ++;
						//System.out.println("Horas pasadas: "+Recursos.horasJugadas);
					}
					
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startHilo() {
		hilo = new Thread(this);
		hilo.start();
	}
}
