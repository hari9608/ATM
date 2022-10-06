package atm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ATM_Process {
	public static Connection con;
	public static PreparedStatement pst;
	public static ResultSet rs;

	public static void create() throws ClassNotFoundException, SQLException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con
				.prepareStatement("create table ATM(DENOMINATION INT(5), NUMBER INT(50), VALUE INT(50))");
		pst.execute();
		System.out.println("table ATM created");
	}

	public static void createCustomer() throws ClassNotFoundException, SQLException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement(
				"create table CUSTOMER(ACC_NO INT(5), ACC_HOLDER_NAME VARCHAR(50), PIN_NUMBER INT(4), ACC_BALANCE INT(10))");
		pst.execute();
		System.out.println("table CUSTOMER created");
	}

	public static void activate() throws ClassNotFoundException, SQLException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement("insert into ATM values(2000, 0, 0)");
		pst.execute();
		PreparedStatement pst1 = con.prepareStatement("insert into ATM values(500, 0, 0)");
		pst1.execute();
		PreparedStatement pst2 = con.prepareStatement("insert into ATM values(100, 0, 0)");
		pst2.execute();
		System.out.println("ATM activated");
	}

	public static void addCustomer() throws ClassNotFoundException, SQLException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement("insert into CUSTOMER values(101, 'Suresh', 2343, 25234)");
		pst.execute();
		PreparedStatement pst1 = con.prepareStatement("insert into CUSTOMER values(102, 'Ganesh', 5432, 34123)");
		pst1.execute();
		PreparedStatement pst2 = con.prepareStatement("insert into CUSTOMER values(103, 'Magesh', 7854, 26100)");
		pst2.execute();
		PreparedStatement pst3 = con.prepareStatement("insert into CUSTOMER values(104, 'Naresh', 2345, 80000)");
		pst3.execute();
		PreparedStatement pst4 = con.prepareStatement("insert into CUSTOMER values(105, 'Harish', 1907, 103400)");
		pst4.execute();
		System.out.println("CUSTOMER ACCS activated");
	}

	public static void addNewCustomer() throws ClassNotFoundException, SQLException {
		try (Scanner sc = new Scanner(System.in)) {
			int acc_no = 106;
			con = DBUtil.getConnect();
			PreparedStatement pst = con.prepareStatement("insert into CUSTOMER values(?,?,?,?)");
			pst.setInt(1, acc_no);
			String name = sc.next();
			pst.setString(2, name);
			int pin = sc.nextInt();
			pst.setInt(3, pin);
			int initial = sc.nextInt();
			pst.setInt(4, initial);
			pst.execute();
			System.out.println("CUSTOMER ACCS activated");
			acc_no++;
		}
	}

	public static void deActivateAcc() throws SQLException, ClassNotFoundException {
		try (Scanner sc = new Scanner(System.in)) {
			con = DBUtil.getConnect();
			PreparedStatement pst = con.prepareStatement("delete from CUSTOMER where acc_no=?");
			int acc_no = sc.nextInt();
			pst.setInt(1, acc_no);
			pst.execute();
			System.out.println("CUSTOMER ACCS deactivated");
		}
	}

	public static void deActivateATM() throws SQLException, ClassNotFoundException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement("delete from ATM");
		pst.execute();
		System.out.println("ATM CLEARED");
	}

	// 1
	public static void load_cash() throws ClassNotFoundException, SQLException {
		try (Scanner sc = new Scanner(System.in)) {
			con = DBUtil.getConnect();
			PreparedStatement pst = con
					.prepareStatement("update ATM set NUMBER=NUMBER+?, VALUE=VALUE+?  where DENOMINATION=? ");
			System.out.println("ENTER THE DENOMINATION TO LOAD ");
			int denomination = sc.nextInt();
			System.out.println("ENTER THE NUMBER OF DENOMINATION TO LOAD ");
			int number = sc.nextInt();
			int value = number * denomination;
			pst.setInt(1, number);
			pst.setInt(2, value);
			pst.setInt(3, denomination);
			pst.executeUpdate();
		}
		System.out.println("cash loaded thank you");
		PreparedStatement pst1 = con.prepareStatement("SELECT * FROM ATM ");
		rs = pst1.executeQuery();
		System.out.println("DENOMINATION    	NUMBER    	VALUE");
		while (rs.next()) {
			System.out.println("	" + rs.getInt(1) + "		" + rs.getInt(2) + "   		 " + rs.getInt(3));
		}

	}

	// 2
	public static void showCustomerDetails() throws SQLException, ClassNotFoundException {
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement("select * from CUSTOMER");
		rs = pst.executeQuery();
		System.out.println("CUSTOMER table");
		while (rs.next()) {
			System.out.println(rs.getInt(1) + "   " + rs.getString(2) + "   " + rs.getInt(3) + "   " + rs.getInt(4));
		}
	}

	// 3.1
	public static void checkBalance() throws SQLException, ClassNotFoundException {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("ENTER THE ACC_NO ");
			int acc_no = sc.nextInt();
			System.out.println("ENTER THE PIN NUMBER ");
			int pin_no = sc.nextInt();
			int pinNo = 0, accBal = 0;
			con = DBUtil.getConnect();
			PreparedStatement pst = con.prepareStatement("select PIN_NUMBER,ACC_BALANCE from CUSTOMER where ACC_NO=?");
			pst.setInt(1, acc_no);
			rs = pst.executeQuery();
			while (rs.next()) {
				pinNo = rs.getInt("PIN_NUMBER");
				accBal = rs.getInt("ACC_BALANCE");
			}
			if (pinNo == pin_no) {
				System.out.print("YOUR ACC NO " + acc_no + " BALANCE " + accBal);
			} else {
				System.out.println("CHECK YOUR PIN TRY AGAIN");
			}
		}
	}

	// 3.2
	public static void withdrawMoney() throws SQLException, ClassNotFoundException {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("ENTER THE ACC_NO ");
			int acc_no = sc.nextInt();
			System.out.println("ENTER THE PIN NUMBER ");
			int pin_no = sc.nextInt();
			System.out.println("ENTER THE AMOUNT TO DEBIT ");
			int debit = sc.nextInt();
			if (debit % 100 != 0) {
				System.out.println("ENTER VALID AMOUNT MULTIPLES OF 100 500");
				return;
			} else if (debit < 100) {
				System.out.println("MINIMUM CASH WITHDRAWN 100");
				return;
			} else if (debit > 10000) {
				System.out.println("DAILY LIMIT EXCEEDED");
				return;
			} else if (AtmBalance() < debit) {
				System.out.println("CASH NOT EXCEEDED YOU ENTERED");
				return;
			}
			int pinNo = 0, accBal = 0;
			con = DBUtil.getConnect();
			PreparedStatement pst = con.prepareStatement("select PIN_NUMBER,ACC_BALANCE from CUSTOMER where ACC_NO=?");
			pst.setInt(1, acc_no);
			rs = pst.executeQuery();
			while (rs.next()) {
				pinNo = rs.getInt("PIN_NUMBER");
				accBal = rs.getInt("ACC_BALANCE");
			}
			if (pinNo == pin_no && accBal >= debit) {
				accBal = accBal - debit;
				System.out.println("YOUR ACC NO " + acc_no + " BALANCE " + accBal);
				PreparedStatement pst1 = con.prepareStatement("update CUSTOMER set ACC_BALANCE=? where ACC_NO=?");
				pst1.setInt(1, accBal);
				pst1.setInt(2, acc_no);
				pst1.executeUpdate();
				System.out.println("WITHDRAWL SUCCESSFULLY");
				if (debit % 2000 == 0) {
					int c = debit / 2000;
					PreparedStatement pst2 = con
							.prepareStatement("update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
					int value = c * 2000;
					pst2.setInt(1, c);
					pst2.setInt(2, value);
					pst2.setInt(3, 2000);
					pst2.executeUpdate();
				} else if (debit % 1000 == 0 && debit > 3000) {
					int c = debit / 2000;
					PreparedStatement pst2 = con
							.prepareStatement("update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
					int value = c * 2000;
					pst2.setInt(1, c);
					pst2.setInt(2, value);
					pst2.setInt(3, 2000);
					pst2.executeUpdate();
					PreparedStatement pst3 = con
							.prepareStatement("update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
					pst3.setInt(1, 2);
					pst3.setInt(2, 1000);
					pst3.setInt(3, 500);
					pst3.executeUpdate();
				} else {
					int c = debit / 2000;
					debit -= c * 2000;
					PreparedStatement pst2 = con
							.prepareStatement("update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
					int value = c * 2000;
					pst2.setInt(1, c);
					pst2.setInt(2, value);
					pst2.setInt(3, 2000);
					pst2.executeUpdate();
					if (debit >= 1000) {
						debit -= 1000;
						PreparedStatement pst3 = con.prepareStatement(
								"update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
						pst3.setInt(1, 2);
						pst3.setInt(2, 1000);
						pst3.setInt(3, 500);
						pst3.executeUpdate();
					}
					int d = debit / 100;
					PreparedStatement pst4 = con
							.prepareStatement("update ATM set NUMBER=NUMBER-?, VALUE=VALUE-?  where DENOMINATION=? ");
					pst4.setInt(1, d);
					pst4.setInt(2, debit);
					pst4.setInt(3, 100);
					pst4.executeUpdate();
				}

				return;
			} else if (accBal < debit) {
				System.out.println("INSUFFICIENT AMOUNT IN YOUR ACC");
				return;
			} else {
				System.out.println("CHECK YOUR PIN TRY AGAIN");
				return;
			}
		}

	}

	// 3.3
	public static void transferMoney() throws SQLException, ClassNotFoundException {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("ENTER THE SENDER ACC_NO ");
			int send_acc_no = sc.nextInt();
			System.out.println("ENTER THE PIN NUMBER ");
			int pin_no = sc.nextInt();
			System.out.println("ENTER THE RECEIVE ACC_NO ");
			int rece_acc_no = sc.nextInt();
			System.out.println("ENTER THE AMOUNT TO TRANSFER ");
			int trans = sc.nextInt();
			if (trans < 1000) {
				System.out.println("MINIMUM CASH TRANSFER 1000");
				return;
			} else if (trans > 10000) {
				System.out.println("DAILY TRANSFER LIMIT EXCEEDED");
				return;
			}
			int pinNo = 0, accBal = 0;
			con = DBUtil.getConnect();
			PreparedStatement pst = con.prepareStatement("select PIN_NUMBER,ACC_BALANCE from CUSTOMER where ACC_NO=?");
			pst.setInt(1, send_acc_no);
			rs = pst.executeQuery();
			while (rs.next()) {
				pinNo = rs.getInt("PIN_NUMBER");
				accBal = rs.getInt("ACC_BALANCE");
			}
			if (pinNo == pin_no && accBal >= trans) {
				accBal = accBal - trans;
				System.out.println("YOUR ACC NO " + send_acc_no + " \nBALANCE " + accBal);
				PreparedStatement pst1 = con.prepareStatement("update CUSTOMER set ACC_BALANCE=? where ACC_NO=?");
				pst1.setInt(1, accBal);
				pst1.setInt(2, send_acc_no);
				pst1.executeUpdate();
				PreparedStatement pst2 = con
						.prepareStatement("update CUSTOMER set ACC_BALANCE=ACC_BALANCE+? where ACC_NO=?");
				pst2.setInt(1, trans);
				pst2.setInt(2, rece_acc_no);
				pst2.executeUpdate();
				System.out.println("TRANSFERED SUCCESSFULLY");
			} else {
				System.out.println("CHECK YOUR PIN TRY AGAIN");
			}
		}
	}

	// 3.4
	public static void checkAtmBalance() throws SQLException, ClassNotFoundException {
		con = DBUtil.getConnect();
		PreparedStatement pst1 = con.prepareStatement("SELECT * FROM ATM ");
		rs = pst1.executeQuery();
		System.out.println("DENOMINATION    	NUMBER    	VALUE");
		while (rs.next()) {
			System.out.println("	" + rs.getInt(1) + "		" + rs.getInt(2) + "   		 " + rs.getInt(3));
		}

		PreparedStatement pst = con.prepareStatement("select sum(value) from atm");
		rs = pst.executeQuery();
		while (rs.next()) {
			System.out.println("\n" + rs.getInt(1) + "   ");
		}
		System.out.println("thank you");
	}

	public static int AtmBalance() throws SQLException, ClassNotFoundException {
		int bal = 0;
		con = DBUtil.getConnect();
		PreparedStatement pst = con.prepareStatement("select sum(value) as bal from atm");
		rs = pst.executeQuery();
		while (rs.next()) {
			bal = rs.getInt("bal");
		}
		return bal;
	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		System.out.println("ROLL NO:20E109\n");
		try (Scanner sc = new Scanner(System.in)) {
			// create(); table ATM created
			// createCustomer(); table CUSTOMER created
			// activate(); ATM activated
			// addCustomer(); CUSTOMER ACCS activated
			// addNewCustomer(); new customer adding
			// deActivateAcc(); deleting unwanted customer
			// deActivateATM(); reset ATM values
			System.out.print("1. Load cash to ATM\n2. Show customer Details\n3. Show ATM operations\n4. ExitS\n");
			System.out.print("ENTER THE CHOICE FROM MENU TO DO ");
			int cho1 = sc.nextInt();
			switch (cho1) {
			case 1:
				load_cash();
				break;
			case 2:
				showCustomerDetails();
				break;
			case 3:
				System.out.print(
						"1. Check Balance\n2. Withdraw Money\n3. Transfer Money\n4. Check ATM Balance\n5. ExitS\n");
				System.out.print("ENTER THE CHOICE FROM MENU TO DO ");
				int cho2 = sc.nextInt();
				switch (cho2) {
				case 1:
					checkBalance();
					break;
				case 2:
					withdrawMoney();
					break;
				case 3:
					transferMoney();
					break;
				case 4:
					checkAtmBalance();
					break;
				case 5:
					System.out.print("PROGRAM TERMINATTED!!!");
					break;
				default:
					System.out.println("ENTER ANOTHER CHOICE.");
				}
				break;
			case 4:
				System.out.print("PROGRAM TERMINATTED!!!");
				break;
			default:
				System.out.println("ENTER ANOTHER CHOICE.");
			}
		}
	}
}
