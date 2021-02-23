import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import rx.Observable

object ReactiveMongoDriver {
    private const val DATABASE_NAME = "product_list"
    private const val USER_COLLECTION = "users"
    private const val PRODUCT_COLLECTION = "products"

    private val client: MongoClient = MongoClients.create()
    private val db: MongoDatabase = client.getDatabase(DATABASE_NAME)

    fun addUser(user: User): Observable<Success> = db
        .getCollection(USER_COLLECTION)
        .insertOne(user.toDocument())

    fun addProduct(product: Product): Observable<Success> = db
        .getCollection(PRODUCT_COLLECTION)
        .insertOne(product.toDocument())

    fun getUserById(id: Int): Observable<User> = db
        .getCollection(USER_COLLECTION)
        .find(Filters.eq("id", id))
        .toObservable()
        .map(Document::toUser)

    fun getAllUsers(): Observable<User> = db
        .getCollection(USER_COLLECTION)
        .find()
        .toObservable()
        .map(Document::toUser)

    fun getAllProducts(): Observable<Product> = db
        .getCollection(PRODUCT_COLLECTION)
        .find()
        .toObservable()
        .map(Document::toProduct)
}
