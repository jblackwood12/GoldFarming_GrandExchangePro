import java.awt.Rectangle;

import org.rsbuddy.widgets.GrandExchange;

import com.rsbuddy.script.methods.Bank;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Npc;


public class GeInteract {
	public static int grand_exchange_clerk_1 = 6529;
	public static int grand_exchange_clerk_2 = 6528;
	
public static void pause(int time_to_sleep) {
		try {
			Thread.sleep(Random.nextInt(time_to_sleep + 20, time_to_sleep));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
public static void open_grand_exchange() {
		//try quickly to open bank
		final Npc ge_clerk_1 = Npcs.getNearest(grand_exchange_clerk_1);
		if(GrandExchange.isOpen() == false)
		{
			ge_clerk_1.interact("Exchange " + ge_clerk_1.getName());
			pause(7000);
		}
		
		//may have to enter pin if ge isn't open by now
		if(GrandExchange.isOpen() == false)
		{
			pause(20000);
			ge_clerk_1.interact("Exchange " + ge_clerk_1.getName());
		}
		
		if(GrandExchange.isOpen() == false){
			pause(20000);
			ge_clerk_1.interact("Exchange " + ge_clerk_1.getName());
		}
		
		if(GrandExchange.isOpen() == false)
		{
			pause(5000);
			ge_clerk_1.interact("Exchange " + ge_clerk_1.getName());
		}
		
		if(GrandExchange.isOpen() == false)
		{
			pause(5000);
			ge_clerk_1.interact("Exchange " + ge_clerk_1.getName());
		}	
		
		
	}

public static void close_grand_exchange() {
	if(GrandExchange.isOpen()){
		Widgets.getComponent(105, 14).click();
		pause(5000);
	}
	
	if(GrandExchange.isOpen()){
		Mouse.moveSlightly();
		pause(2000);
		Widgets.getComponent(105, 14).click();
		pause(4000);
	}
	
	if(GrandExchange.isOpen()){
		Mouse.moveSlightly();
		pause(2000);
		Widgets.getComponent(105, 14).click();
		pause(4000);
	}
	
}
public static void type(String text, boolean flag) {
	com.rsbuddy.script.methods.Keyboard.sendText(text, flag);
	pause(1000);
}

public static void type(int amount, boolean flag) {
	com.rsbuddy.script.methods.Keyboard.sendText(Integer.toString(amount),
			flag);
	pause(1000);
}


public static void view_buy_offer(int the_button) {
	switch (the_button) {

	case 0:
		Widgets.getComponent(105, 19).click();
		pause(2000);
		break;

	case 1:
		Widgets.getComponent(105, 35).click();
		pause(2000);
		break;

	case 2:
		Widgets.getComponent(105, 51).click();
		pause(2000);
		break;

	case 3:
		Widgets.getComponent(105, 70).click();
		pause(2000);
		break;

	case 4:
		Widgets.getComponent(105, 89).click();
		pause(2000);
		break;

	case 5:
		Widgets.getComponent(105, 108).click();
		pause(2000);
		break;
	}
}

public static void view_sell_offer(int the_button) {
	switch (the_button) {

	case 0:
		Widgets.getComponent(105, 20).click();
		pause(2000);
		break;

	case 1:
		Widgets.getComponent(105, 36).click();
		pause(2000);
		break;

	case 2:
		Widgets.getComponent(105, 52).click();
		pause(2000);
		break;

	case 3:
		Widgets.getComponent(105, 71).click();
		pause(2000);
		break;

	case 4:
		Widgets.getComponent(105, 90).click();
		pause(2000);
		break;

	case 5:
		Widgets.getComponent(105, 109).click();
		pause(2000);
		break;
	}
}

public static int get_inv_cash() {
	com.rsbuddy.script.wrappers.Item[] inventory_array = Inventory.getItems();
	int inv_cash = 0;
	for (int i = 0; i < inventory_array.length; i++) {
		if (inventory_array[i].getId() == 995) {
			inv_cash = inventory_array[i].getStackSize();
		}
	}
	if (inv_cash > 0) {
		return inv_cash;
	} else {
		return -1;
	}

}

public static void make_buy_offer(int the_button, String item_name, int quantity, int price) {

	boolean not_arrived = true;
	
	while(not_arrived){
	switch (the_button) {
	case 0:
		Widgets.getComponent(105, 31).click();
		pause(1500);
		break;
	case 1:
		Widgets.getComponent(105, 47).click();
		pause(1500);
		break;
	case 2:
		Widgets.getComponent(105, 63).click();
		pause(1500);
		break;
	case 3:
		Widgets.getComponent(105, 82).click();
		pause(1500);
		break;
	case 4:
		Widgets.getComponent(105, 101).click();
		pause(1500);
		break;
	case 5:
		Widgets.getComponent(105, 120).click();
		pause(1500);
		break;
	}
	pause(1500);
	
	if(GrandExchange.isSellScreenOpen() == true){
		abort_sell_offer();
	}
		
	if(GrandExchange.isBuyScreenOpen() == false){
		close_grand_exchange();
		open_grand_exchange();
	}else{
		not_arrived = false;
		pause(5000);
	}
	
	}
	
	pause(1500);

	if(GrandExchange.isBuyScreenOpen()){
	if (the_button >= 0 && the_button < 6) {
		pause(3000);
		type(item_name, false);
		pause(3000);
		
		Rectangle rect = Widgets.getComponent(137, 3).getBoundingRect();
		int x_loc = (int) rect.getX();
		int y_loc = (int) rect.getY();
		Mouse.click(x_loc + Random.nextInt(100, 400), y_loc + Random.nextInt(1, 10), true);
		pause(1000);
		
		if (quantity > 0) {
			add_quantity(quantity);
			change_price(price);
			confirm_offer();
		}
	}
	}
}

public static void make_sell_offer(int the_button, int item_id, int quantity, int price) {
	
		boolean not_arrived = true;
		
		while(not_arrived){
		
		switch (the_button) {
		case 0:
			Widgets.getComponent(105, 32).click();
			pause(1500);
			break;
		case 1:
			Widgets.getComponent(105, 48).click();
			pause(1500);
			break;
		case 2:
			Widgets.getComponent(105, 64).click();
			pause(1500);
			break;
		case 3:
			Widgets.getComponent(105, 83).click();
			pause(1500);
			break;
		case 4:
			Widgets.getComponent(105, 102).click();
			pause(1500);
			break;
		case 5:
			Widgets.getComponent(105, 121).click();
			pause(1500);
			break;
		}
		
		if(GrandExchange.isBuyScreenOpen() == true){
			abort_buy_offer();
		}
			
		if(GrandExchange.isSellScreenOpen() == false){
			close_grand_exchange();
			open_grand_exchange();
		}else{
			not_arrived = false;
			pause(5000);
		}
		
		}
	
		pause(2000);
		
		if(GrandExchange.isSellScreenOpen()){
		if(the_button >= 0 && the_button < 6){
			pause(3000);
			Item click_this_item = Inventory.getItem(item_id);
			pause(1000);
			if(click_this_item != null)
			{
				click_this_item.click(true);
			}
			pause(2000);
			if(quantity > 0){
				add_quantity(quantity);
				change_price(price);
				confirm_offer();
			}
		}
		}
	}

public static void back_to_offer_summary() {
	Widgets.getComponent(128).click();
	pause(1000);
}

public static void confirm_offer() {
	Widgets.getComponent(105, 187).click();
	pause(3000);
}

public static void abort_sell_offer(){
	if(GrandExchange.isSellScreenOpen()){
		int num_abort_clicks = Random.nextInt(4, 2);
		for(int i = 0; i < num_abort_clicks; i++ ){
			Widgets.getComponent(105, 200).click();
			pause(6000/num_abort_clicks);
		}
		//collect2
		Widgets.getComponent(105, 208).click();
		pause(500);
	
		//collect1
		Widgets.getComponent(105, 206).click();
		pause(1500);
		
	}else{
		close_grand_exchange();
		open_grand_exchange();
	}
	
	
}


public static void abort_buy_offer(){
	if(GrandExchange.isBuyScreenOpen()){
		int num_abort_clicks = Random.nextInt(4, 2);
		for(int i = 0; i < num_abort_clicks; i++ ){
			Widgets.getComponent(105, 200).click();
			pause(6000/num_abort_clicks);
		}
		//collect2
		Widgets.getComponent(105, 208).click();
		pause(500);
	
		//collect1
		Widgets.getComponent(105, 206).click();
		pause(1500);
		
		
	}else{
		close_grand_exchange();
		open_grand_exchange();
	}
	
	
	
}


public static void open_bank() {

	// try to open quickly.
	if (Bank.isOpen() == false) {
		Bank.open();
		pause(5000);
	}

	// wait until the player has made it to the bank.
	while (Players.getLocal().isMoving()) {
		pause(1000);
	}

	// if it doesn't open immediately, we probably have to enter our pin.
	// give it time to complete this task.
	if (Bank.isOpen() == false) {
		pause(20000);
		Bank.open();
	}

	// if the bank is not opening
	// move the mouse slightly so that the mouse is outside of the
	// component.
	// this way the "Bank.open()" function will move the mouse to the "x"
	// again.
	while (Bank.isOpen() != true) {
		com.rsbuddy.script.methods.Mouse.moveSlightly();
		pause(2000);
		Bank.open();
	}

}

public static void close_bank() {
	int attempts = 0;
	while (Bank.isOpen() && attempts < 5) {
		Bank.close();
		pause(1500);
		attempts++;
	}

	if (Bank.isOpen()) {
		com.rsbuddy.script.methods.Mouse.moveSlightly();
		pause(1000);
		Bank.close();
		pause(1000);
	}

	if (Bank.isOpen()) {
		com.rsbuddy.script.methods.Mouse.moveSlightly();
		pause(1000);
		Bank.close();
		pause(1000);
	}
}

public static void deposit_inventory() {
	if (Bank.isOpen()) {
		if (Inventory.getCount() != 0) {
			Bank.depositAll();
			pause(300);
		}
	}
}
public static void take_out_item(int id_of_item, int quantity) {

	// make sure the bank is open and there is room in your inventory
	if (Bank.isOpen() && (Inventory.isFull() != true)) {

		Bank.withdraw(id_of_item, quantity);
		// keep trying to withdraw until the item appears in your inventory
		// or after a number of attempts
		int attempts = 3;

		while (Inventory.contains(id_of_item) == false && attempts < 3) {
			// withdraw the item with the specified quantity
			pause(500);
			Bank.withdraw(id_of_item, quantity);
			++attempts;
		}

		if (Inventory.contains(id_of_item) == false) {
			pause(500);
			Bank.withdraw(id_of_item, quantity);
			pause(500);
		}

	}
	pause(1000);
}

public static void add_quantity(int quantity) {
	switch (quantity) {

	case 1:
		Widgets.getComponent(105, 160).click();
		pause(1000);
		break;

	case 10:
		Widgets.getComponent(105, 162).click();
		pause(1000);
		break;

	case 100:
		Widgets.getComponent(105, 164).click();
		pause(1000);
		break;

	case 1000:
		Widgets.getComponent(105, 166).click();
		pause(1000);
		break;

	default:
		Widgets.getComponent(105, 168).click();
		pause(3000);
		type(quantity, true);
		pause(3000);
		break;

	}
}


public static void change_price(int price) {
		Widgets.getComponent(105, 177).click();
		pause(2000);
		type(price, true);
		pause(2000);
}

public static int count_num_items(int item_id_in){
	int quantity = 0;
	
	if(Inventory.contains(item_id_in)){
		quantity += Inventory.getCount(item_id_in);
	}
	
	if(Inventory.contains(item_id_in+1)){
		Item noted_item = Inventory.getItem(item_id_in+1);
		if(noted_item != null){
			quantity += noted_item.getStackSize();
		}
	}
	
	return quantity;
	
	
}


public static boolean check_sell_offer_page(){
	if(org.rsbuddy.widgets.GrandExchange.isSellScreenOpen() == false){
		pause(10000);
		//make sure grand exchange or any interface is actually closed
		com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
		pause(10000);
		
		//disable the script that logs us back in
		com.rsbuddy.script.methods.Environment.disableRandoms();
		//completely log out
		pause(5000);
		Game.logout(false);
		pause(5000);
		return true;
	}else{
		return false;
	}
}

public static boolean check_buy_offer_page(){
	if(org.rsbuddy.widgets.GrandExchange.isBuyScreenOpen() == false){
		pause(10000);
		//make sure grand exchange or any interface is actually closed
		com.rsbuddy.script.methods.Environment.enableRandom("InterfaceCloser");
		pause(10000);
		//disable the script that logs us back in
		com.rsbuddy.script.methods.Environment.disableRandoms();
		//completely log out
		pause(5000);
		Game.logout(false);
		pause(5000);
		return true;
	}else{
		return false;
	}
}


}
