package dds.monedero.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

public class Cuenta {

	private double saldo = 0;
	private List<Movimiento> movimientos = new ArrayList<>();

	public Cuenta() {
		saldo = 0;
	}

	public Cuenta(double montoInicial) {
		saldo = montoInicial;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public void poner(double cuanto) {
		if (cuanto <= 0) {
			throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
		}

		if (getMovimientos().stream().filter(Movimiento::isDeposito).count() >= 3) {
			throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
		}

		new Movimiento(LocalDate.now(), cuanto, true).agregateA(this);
	}

	public void sacar(double cuanto) {
		if (cuanto <= 0) {
			throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
		}
		if (getSaldo() - cuanto < 0) {
			throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
		}
		double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
		double limite = 1000 - montoExtraidoHoy;
		if (cuanto > limite) {
			throw new MaximoExtraccionDiarioException(
					"No puede extraer mas de $ " + 1000 + " diarios, límite: " + limite);
		}
		new Movimiento(LocalDate.now(), cuanto, false).agregateA(this);
	}

	/*
	 * ¿Es responsabilidad de este método crear el movimiento? ¿Si lo que yo quiero
	 * es _agregar un un movimiento_, no deberia pasar el movimiento como parámetro?
	 * Code Smell => Long parameter List
	 */
	public void agregarMovimiento(Movimiento mov) {

		// copio el movimiento
		Movimiento new_mov = new Movimiento(mov.getFecha(), mov.getMonto(), mov.isDeposito());
		movimientos.add(new_mov);
		
		// debo actualizar el saldo
		int mult = mov.isDeposito() ? 1 : -1;
		this.saldo = (mult * mov.getMonto()) + this.saldo;
	}

	public double getMontoExtraidoA(LocalDate fecha) {
		return getMovimientos().stream()
				.filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
				.mapToDouble(Movimiento::getMonto).sum();
	}

	public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

}
