package dds.monedero.model;

public class MovTypeDepositoStrat implements IMovementType {

	@Override
	public int getMovTypeMult() {
		return 1;
	}

	@Override
	public Boolean isExtraccion() {
		return false;
	}

	@Override
	public Boolean isDeposito() {
		return true;
	}
	

}
