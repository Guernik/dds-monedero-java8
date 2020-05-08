package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
	private LocalDate fecha;
	// En ningún lenguaje de programación usen jamás doubles para modelar dinero en
	// el mundo real
	// siempre usen numeros de precision arbitraria, como BigDecimal en Java y
	// similares
	private double monto;	
	private IMovementType movTypeStrategy;
	

	public Movimiento(LocalDate fecha, double monto, IMovementType mov_type_strat) {
		this.fecha = fecha;
		this.monto = monto;
		this.movTypeStrategy = mov_type_strat;

	}

	public double getMonto() {
		return monto;
	}

	public LocalDate getFecha() {
		return fecha;
	}
	
	public Boolean isDeposito() {
		return this.movTypeStrategy.isDeposito();
	}
	
	public Boolean isExtraccion() {
		return this.movTypeStrategy.isExtraccion();
	}

	public boolean fueDepositado(LocalDate fecha) {
		return isDeposito() && esDeLaFecha(fecha);
	}

	public boolean fueExtraido(LocalDate fecha) {
		return isExtraccion() && esDeLaFecha(fecha);
	}

	public boolean esDeLaFecha(LocalDate fecha) {
		return this.fecha.equals(fecha);
	}

	
	public int getMovTypeMultiplier() {
		return this.movTypeStrategy.getMovTypeMult();
	}
	

	public void agregateA(Cuenta cuenta) {
		cuenta.agregarMovimiento(this);
	}
	
	public IMovementType getMovTypeStrat() {
		// la mov strategy es inmutable así que me animo a esto.
		return this.movTypeStrategy;
	}

	
}
