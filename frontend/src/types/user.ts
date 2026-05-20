export interface User {
  email: string
  token: string
  username: string
  bio: string | null
  image: string | null
}

export interface UserResponse {
  user: User
}

export interface LoginRequest {
  user: {
    email: string
    password: string
  }
}

export interface RegisterRequest {
  user: {
    username: string
    email: string
    password: string
  }
}

export interface UpdateUserRequest {
  user: {
    email?: string
    password?: string
    username?: string
    bio?: string | null
    image?: string | null
  }
}
