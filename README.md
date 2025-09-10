# Mini Instagram Backend

A Spring Boot backend for a social media application with features like posts, likes, comments, follows, and user profiles.

## 🚀 Features

- **User Management**: Registration, authentication, profile management
- **Posts**: Create, view, like, save posts with images
- **Comments**: Add and view comments on posts
- **Follow System**: Follow/unfollow users
- **File Upload**: Avatar and post image uploads
- **JWT Authentication**: Secure token-based authentication

## 🔧 Setup Instructions

### Prerequisites

- Java 21+
- MySQL 8.0+
- Maven

### Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE ministagram;
```

2. The application will automatically create tables using Hibernate DDL auto-update.

### Configuration

1. **Copy the example configuration:**
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

2. **Update the configuration with your values:**
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ministagram
spring.datasource.username=your_database_username
spring.datasource.password=your_database_password

# JWT Configuration
jwt.secret.key=your_jwt_secret_key_here_make_it_long_and_random

# Email Configuration (optional)
spring.mail.host=smtp.your-email-provider.com
spring.mail.username=your-email@example.com
spring.mail.password=your-email-password
```

### Security Notes

⚠️ **IMPORTANT**: Never commit `application.properties` to version control. It contains sensitive information like database credentials and JWT secrets.

The `.gitignore` file is configured to exclude:
- `application.properties`
- `application-dev.properties`
- `application-prod.properties`
- All `.env` files
- Upload directories

### Running the Application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or using Maven directly
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api/v1.0`

## 📚 API Endpoints

### Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /logout` - User logout

### Posts
- `GET /posts/feed` - Get user's feed
- `GET /posts/{postId}` - Get specific post
- `POST /posts` - Create new post
- `POST /posts/{postId}/like` - Like a post
- `POST /posts/{postId}/unlike` - Unlike a post
- `POST /posts/{postId}/save` - Save a post
- `POST /posts/{postId}/unsave` - Unsave a post
- `GET /posts/liked` - Get user's liked posts
- `GET /posts/saved` - Get user's saved posts

### Comments
- `POST /posts/{postId}/comments` - Add comment
- `GET /posts/{postId}/comments` - Get post comments
- `DELETE /comments/{commentId}` - Delete comment

### Follows
- `POST /follows/follow` - Follow a user
- `POST /follows/unfollow` - Unfollow a user
- `GET /follows/followers/{userId}` - Get user's followers
- `GET /follows/following/{userId}` - Get users followed by user

### Profile
- `GET /profile` - Get current user's profile
- `PUT /profile` - Update profile
- `POST /users/me/avatar` - Upload avatar
- `GET /posts/user/{userId}` - Get user's posts

## 🗄️ Database Schema

The application uses JPA entities:
- `UserModel` - User information
- `PostModel` - Posts with images and captions
- `LikeModel` - Post likes
- `SaveModel` - Saved posts
- `CommentModel` - Post comments
- `FollowModel` - User follows

## 🔒 Security Features

- JWT token authentication
- Password encryption
- CORS configuration
- Input validation
- File upload security
- SQL injection prevention (JPA)

## 📁 Project Structure

```
src/main/java/com/emrecelik/mini_instagram_backend/
├── controller/     # REST API controllers
├── model/         # JPA entities
├── repo/          # Data repositories
├── service/       # Business logic
├── util/          # Utility classes
├── config/        # Configuration classes
├── filter/        # JWT filters
└── io/           # DTOs (Data Transfer Objects)
```

## 🧪 Testing

Run tests with:
```bash
./mvnw test
```

## 📝 License

This project is for educational purposes.
