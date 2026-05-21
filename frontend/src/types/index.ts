export interface Profile {
  username: string
  bio: string | null
  image: string | null
  following: boolean
}

export interface Article {
  slug: string
  title: string
  description: string
  body: string
  tagList: string[]
  createdAt: string
  updatedAt: string
  favorited: boolean
  favoritesCount: number
  author: Profile
}

export interface ArticleResponse {
  article: Article
}

export interface MultipleArticlesResponse {
  articles: Article[]
  articlesCount: number
}

export interface TagsResponse {
  tags: string[]
}
