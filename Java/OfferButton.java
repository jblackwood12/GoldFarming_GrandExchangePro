
public class OfferButton {
int num_items;
boolean isBuy;
boolean isSell;
int the_item;

public OfferButton(){
	num_items = 0;
	isBuy = false;
	isSell = false;
	the_item = -1;
}
	
boolean isFree(){
	if(isBuy == false && isSell == false){
		return true;
	}else{
		return false;
	}	
}

void setBuy(int the_item_in, int num_items_in){
	num_items = num_items_in;
	the_item = the_item_in;
	isBuy = true;
	isSell = false;
}

void setSell(int the_item_in, int num_items_in){
	num_items = num_items_in;
	the_item = the_item_in;
	isSell = true;
	isBuy = false;
}

void setEmpty(){
	num_items = 0;
	isBuy = false;
	isSell = false;
	the_item = -1;
}
}
