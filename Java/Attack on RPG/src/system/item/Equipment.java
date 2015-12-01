package system.item;

import system.AttributeManage;
import system.actor.BaseActor;

/**
 * Item which can be equipped. Example: sword, shield.
 * 
 * @author B97502052, B98505005
 * 
 */
public abstract class Equipment extends BaseItem {

	protected BaseActor target;
	protected AttributeManage numeral;

	public Equipment(String name, ItemType type, String description,
			AttributeManage numeral) {
		super(name, type, description);
		this.numeral = numeral;
	}

	@Override
	public void act() {
		target.getAttributeMax().add(numeral);
		target.getAttributeNow().add(numeral, target.getAttributeMax());
	}

	@Override
	public void stop() {
		target.getAttributeNow().subtract(numeral);
		target.getAttributeMax().subtract(numeral);
	}
	
	@Override
	public void setTarget(BaseActor target){
		this.target = target;
	}
}
