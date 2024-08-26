import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCommitSample_2 {
	
	private static final String URL = "jdbc:mysql://localhost:3306/database01?user=user01&"
			+ "password=password01&useSSL=false&allowPublicKeyRetrieval=true";

	public static void main(String[] args) throws ClassNotFoundException {
		
		Connection connection = null;

		// Connection（データベースとの接続を表す）、PreparedStatement（発行するSQLを表す）を、それぞれ生成します。
		try {
			
			// コネクション取得処理
			connection = DriverManager.getConnection(URL);
				
			
			 // 1. 自動コミットさせない設定
		    connection.setAutoCommit(false);
		    
		    // ↓ユーザーBさんのお金を1,000円減らすSQLを発行
		    PreparedStatement statement = connection.prepareStatement("update user2 set money = money - ? where id = ?");
		    
		    // プレースホルダにパラメータをセット
		    statement.setInt(1, 1000);		// 減らす金額を設定
		    statement.setLong(2, 2);		// ユーザーAさんのIDを指定
		    
		    // SQLを実効
		    statement.executeUpdate();
		    
		    // ↓ユーザーAさんのお金を1,000円増やすSQLを発行
		    statement = connection.prepareStatement("update user2 set money = money + ? where id = ?");
		    
		    // プレースホルダにパラメータをセット
		    statement.setInt(1, 1000);		// 増やす金額を設定
		    statement.setLong(2, 1);		// ユーザーBさんのIDを指定
		    
		    // SQLを実効
		    statement.executeUpdate();
		    
		    
		    // 2. 全てのSQLが成功したので、コミット処理
		    connection.commit();
		    
		    System.out.println("正常にコミットが実行されました");
		    
		    
		} catch (SQLException e) {
			
			// 3. 例外が発生したのでロールバック処理
			try {
				connection.rollback();
				System.out.println("コミットに失敗しロールバックが実行されました。");
			} catch (SQLException e1) {
				// TODO 自動生成されたcatchブロック
				e.printStackTrace();
			}
			
		}
		
		// コミットの成否に関わらず、データベースの内容を表示
        try {
            // user2テーブルのすべてのレコードを選択するSQL文を準備します
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user2");
            ResultSet resultSet = statement.executeQuery(); // クエリを実行
            
            System.out.println("\n一覧表示");
            System.out.println("---------------------------");
            
            // 結果セットを繰り返し、各レコードを表示
            while (resultSet.next()) {
            	System.out.println("ID: " + resultSet.getLong("id") + ", 名前:" + resultSet.getString("name") + ", e-mail:" + resultSet.getString("email") + ", お金: " + resultSet.getInt("money"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

	}

}