import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.util.Random;
import org.rsbuddy.widgets.GrandExchange;

@Manifest(authors = { "jblackwood" }, name = "GexPro", description = "Flips items for a profit!", version = 1.0)
public class GexPro extends ActiveScript implements PaintListener {
	public int withdraw_cash = 1000000000;
	public int beginning_cash = 0;
	
	public long start_time = 0;
	
	public int ending_cash = 0;
	public int cashGained = 0;
	public int unusedGP = 0;
	
	public int num_of_items = 3;
	public int number_of_list;
	
	public long millis = 0;
	public long seconds = 0;
	public long minutes = 0;
	public long hours = 0;
	
	public int current_time = 0;
	public int time_to_logout = 0;
	
	public ItemClass[] the_items;
	public OfferButton[] the_buttons;

	private final Color color1 = new Color(0, 0, 0);
	private final RenderingHints anialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	private final BasicStroke stroke1 = new BasicStroke(1);
	private final Font font1 = new Font("Arial", 0, 12);
	private final Font font2 = new Font("Arial", 0, 18);
	private final Font font3 = new Font("Arial", 0, 9);
	private final Image img1 = getImage("http://pictat.com/i/2011/6/1/25896goldbarsli.jpg");
	
	private Image getImage(String url) {
	        try {
	            return ImageIO.read(new URL(url));
	        } catch(IOException e) {
	            return null;
	        }
	    }
	
	public void onRepaint(Graphics g1){
		Graphics2D g = (Graphics2D)g1;
		
		g.setRenderingHints(anialiasing);
		g.setColor(color1);
		g.setStroke(stroke1); 
		
		millis = System.currentTimeMillis() - start_time;
		hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		minutes = millis / (1000 * 60);
		String mins = "" + minutes;
		while (mins.length() < 2) {
			mins = "0" + mins;
		}
		millis -= minutes * (1000 * 60);
		seconds = millis / 1000;
		String secs = "" + seconds;
		while (secs.length() < 2) {
			secs = "0" + secs;
		}
		float xpsec = 0;
		if ((minutes > 0 || hours > 0 || seconds > 0) && cashGained != 0){
			xpsec = ((float) cashGained) / (float) (seconds + (minutes * 60) + (hours * 60 * 60));
		}
		float xpmin = xpsec * 60;
		float xphour = xpmin * 60;
		
		current_time = ((int)hours * 60) + ((int)minutes);

        g.setColor(color1);
        g.drawImage(img1, 539, 273, null);
        g.setFont(font2);
        g.drawString("GexPro", 560, 313);
        g.drawString("By JBlackwood", 565, 328);
        g.setFont(font1);
        g.drawString("Time Ran:" + hours + ":" + mins + ":" + secs, 550, 361);
        g.drawString("Total Profit:" + cashGained, 550, 374);
        g.drawString("GP/HOUR:" + (int)xphour, 550, 387);
        g.drawString("Starting GP: " + beginning_cash, 550, 400);
        g.drawString("Current GP: " + ending_cash , 550, 413);
        g.drawString("Unused GP: " + unusedGP, 550, 426); 
        g.setFont(font3);
        
        for(int i = 0; i < the_items.length; i++){
        g.drawString(the_items[i].name_item + ": " + the_items[i].total_profit, 550, (436 + i * 10));
        }
        
	}
	
	public void calculate_total_cash(){
		int temp_cash = GeInteract.get_inv_cash();
		unusedGP = temp_cash;
		for(int j = 0; j < 3; j++){
			int avg_price = (the_items[j].buy_price + the_items[j].sell_price)/2;
			if(the_buttons[j].isFree() == false){
				temp_cash += the_buttons[j].num_items * the_items[j].buy_price;
			}
			if(the_buttons[j+3].isFree() == false){
				temp_cash += the_buttons[j+3].num_items * avg_price;
			}
		}
		ending_cash = temp_cash;
		cashGained = ending_cash - beginning_cash;
	}
	
	
	public boolean onStart() {
		
		if (!Game.isLoggedIn()) {
			log("Start script logged in");
			return false;
		}

		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {

					// get the number of items
					do {
						String temp0 = JOptionPane.showInputDialog(null,
								"Enter in the number of the list to flip from (1-10)",
								"Type List Number", JOptionPane.QUESTION_MESSAGE);
						try {
							//store the number of the list chosen
							number_of_list = Integer.parseInt(temp0);

						} catch (NumberFormatException ignored) {
						}
					} while (num_of_items < 0 && num_of_items > 10);
				}
			});
		} catch (Exception ignored) {
		}

		return true;
	}

	/**
	 * THIS IS THE HEART OF THE PROGRAM:
	 * Find Starting cash and time.
	 * Get button & item objects ready
	 * Open the Grand Exchange
	 * Check for errors
	 * Merchant until the program is closed
	 */
	public int loop() {
		com.rsbuddy.script.methods.Environment.disableRandom("InterfaceCloser");
		start_time = System.currentTimeMillis();
		beginning_cash = GeInteract.get_inv_cash();
		instantiate_buttons();
		instantiate_items();
		calculate_total_cash();
		GeInteract.open_grand_exchange();
		check_error();
		merch();
		check_error();
		return -1;
	}

	/**
	 * THIS IS WHERE THE ACTUAL FLIPPING IS DONE:
	 * Make sure that the selling prices are above the buying prices
	 * Keep track of how many times the list has been run
	 * Continuously flip items until an error occurs
	 * Call the correct flip function
	 */
	public void merch(){
		update_logout_time();
		
		for (int i = 0; i < num_of_items; i++) {
			calculate_total_cash();
			int initial_amt = ending_cash;
			if((the_items[i].limit < 100)&&(!the_items[i].item_type.equals("Nex"))){
				get_sell_price(i);
				get_buy_price(i);
				the_items[i].swap_prices();
			}
			calculate_total_cash();
			the_items[i].total_profit += (ending_cash - initial_amt);
		}
		
		int loop_num = 0;
		while(loop_num > -1){
			for (int i = 0; i < the_items.length; i++) {
				calculate_total_cash();
				int initial_amt = ending_cash;
				switch(the_items[i].limit){
				case 2:
					if(the_items[i].item_type.equals("Nex")){
						flipnex(i, loop_num);
					}else{
						flip2(i,loop_num);
					}
					
					break;
				case 10:
					flip10(i, loop_num);
					break;
				case 100:
					flip_big(i, loop_num, 100);
					break;
				case 500:
					flip_big(i, loop_num, 500);
					break;
				case 1000:
					flip_big(i, loop_num, 1000);
					break;
				case 5000:
					flip_big(i, loop_num, 5000);
					break;
				case 10000:
					flip_big(i, loop_num, 10000);
					break;
				case 20000:
					flip_big(i, loop_num, 20000);
					break;
				case 25000:
					flip_big(i, loop_num, 25000);
					break;
				
				default:
						break;
				}
				calculate_total_cash();
				the_items[i].total_profit += (ending_cash - initial_amt);
				the_items[i].update_profit(loop_num);
				//log(the_items[i].name_item + ": " + the_items[i].profit);
		}
			
		six_hour_logout_rule();
			
		loop_num += 1;
			if(check_error() == true){
				break;
			}
		}
	}
	/**
	 * Make the button objects
	 */
	public void instantiate_buttons() {
		the_buttons = new OfferButton[6];
		for (int i = 0; i < 6; i++) {
			the_buttons[i] = new OfferButton();
		}
	}
	
	/**
	 * Initialize the items with hard-coded values
	 */
	public void instantiate_items(){
		the_items = new ItemClass[num_of_items];
		switch(number_of_list){
		case 1:
			the_items[0] = new ItemClass("Bandos godsword", 10, 11696, 0);				
			the_items[1] = new ItemClass("Dragonfire shield", 10, 11284, 0); 	
			the_items[2] = new ItemClass("Amulet of fury", 10, 6585, 0); 		
			break;
		case 2:
			the_items[0] = new ItemClass("Dragon pickaxe", 10, 15259, 0);
			the_items[1] = new ItemClass("Barrows - dharok's set", 10, 11848, 0);
			the_items[2] = new ItemClass("Dragon claws", 10, 14484, 0);
			break;
		case 3:
			the_items[0] = new ItemClass("Barrows - guthan's set", 10, 11850, 0);	
			the_items[1] = new ItemClass("Bandos chestplate", 10, 11724, 0);
			the_items[2] = new ItemClass("Bandos tassets", 10, 11726, 0);	
			break;
		case 4:	
			the_items[0] = new ItemClass("Spectral spirit shield", 2,13744 , "Special", 345675, 0);
			the_items[1] = new ItemClass("Arcane spirit shield", 2,13738 , "Special", 346453, 0);	
			the_items[2] = new ItemClass("Ranger boots", 2,2577 , "Special", 248374, 0); 
			break;
		case 5:
			the_items[0] = new ItemClass("Torva full helm", 2, 20135, "Nex", 798123, 0);
			//the_items[0] = new ItemClass("Corrupt vesta's longsword", 2, 13923, "Nex", 342120, 0);
			//the_items[2] = new ItemClass("Armadyl Godsword", 2, 11694, "Special", 69321, 0);
			the_items[1] = new ItemClass("Robin hood hat", 2, 2581, "Special", 59123, 0);
			the_items[2] = new ItemClass("Saradomin armour set", 2, 11928, "Special", 34999, 0);
			break;
		case 6:
			the_items[0] = new ItemClass("Ancient ceremonial mask", 2, 20125, "Special", 89312, 0);
			the_items[1] = new ItemClass("Robin hood hat", 2, 2581, "Special", 59123, 0);
			the_items[2] = new ItemClass("Saradomin armour set", 2, 11928, "Special", 34999, 0);
			break;
		case 7:
			the_items[0] = new ItemClass("Armadyl chestplate", 2, 11720, "Special", 78777, 0);
			the_items[1] = new ItemClass("Armadyl chainskirt", 2, 11722, "Special", 74133, 0);
			the_items[2] = new ItemClass("Armadyl helmet", 2, 11718, "Special", 59311, 0);
			break;
		case 8:
			the_items[0] = new ItemClass("Saradomin sword", 10, 11730, 0);
			the_items[1] = new ItemClass("Dragon hatchet", 10, 6739, 0);
			the_items[2] = new ItemClass("Staff of light", 10, 15486, 0);	
			break;
		case 9:
			the_items[0] = new ItemClass("Armadyl Godsword", 2, 11694, "Special", 69321, 0);
			the_items[1] = new ItemClass("Saradomin Godsword", 2, 11698, "Special", 69888, 0);
			the_items[2] = new ItemClass("Zamorak Godsword", 2, 11700, "Special", 69323, 0);
			break;
		case 10:
			the_items[0] = new ItemClass("Dragon bones", 10000, 536, "",30, 1);
			the_items[1] = new ItemClass("Frost Dragon Bones", 1000, 18832,"", 100, 0);
			the_items[2] = new ItemClass("Saradomin brew", 1000, 6687,"",150, 2);
			break;
		case 11:
			the_items[0] = new ItemClass("Amulet of glory", 100, 1704, "", 499, 0);
			the_items[1] = new ItemClass("Green dragonhide", 10000, 1753, "", 29, 0);
			the_items[2] = new ItemClass("Blue dragonhide", 10000, 1751,"",29, 0);
			break;
		case 12:
			the_items[0] = new ItemClass("Dragon full helm", 2, 11335, "", 87999, 0);
			the_items[1] = new ItemClass("Verac's helm",2 ,4753 , "", 79111 , 0);
			the_items[2] = new ItemClass("Infinity robes set", 2, 11874,"",99121, 0);
			break;
		case 13:
			the_items[0] = new ItemClass("Dragon platebody", 10, 14479, 0);
			the_items[1] = new ItemClass("Dragon chain armour set", 10, 11842, 0);
			the_items[2] = new ItemClass("Dragon chainbody", 10, 3140, 1);	
			break;
		case 14:
			the_items[0] = new ItemClass("Barrows - karil's set", 2, 11852, "", 86111, 0);
			the_items[1] = new ItemClass("Barrows - ahrim's set",2 ,11846 , "", 91333 , 0);
			the_items[2] = new ItemClass("Barrows - verac's set", 2, 11856,"",79111, 0);
			break;
		case 15:
			the_items[0] = new ItemClass("Karil's top", 2, 4736, "", 90111, 0);
			the_items[1] = new ItemClass("Dharok's helm",2 ,4716 , "", 78111 , 0);
			the_items[2] = new ItemClass("Mages' book", 2, 6889, "", 87121, 0);
			break;
		case 16:
			the_items[0] = new ItemClass("Berserker ring", 2, 6737, "", 76431, 0);
			the_items[1] = new ItemClass("Archers' ring",2 ,6733 , "", 48111 , 0);
			the_items[2] = new ItemClass("Master wand", 2, 6914, "", 89120, 0);
			break;
		case 17:
			the_items[0] = new ItemClass("Infinity gloves",2 ,6922 , "", 45999 , 0);
			the