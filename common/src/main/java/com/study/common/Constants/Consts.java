package com.study.common.Constants;

public final class Consts {
    public static final String ADMIN = "ADMIN";
    public static final String MENTOR = "MENTOR";
    public static final String USER = "USER";
    public static final String GET_USER = "/api/user/profile";
    public static final String GET_RATING = "/api/user/top";
    public static final String REGISTER_USER = "/api/user/register";
    public static final String ASSIGN_ROLES = "/api/user/{userId}";
    public static final String LOGIN_USER = "/api/user/login";
    public static final String LOGOUT = "/api/user/logout";
    public static final String CATEGORIES = "/api/learning/categories";
    public static final String EDIT_CATEGORY = "/api/learning/categories/{id}";
    public static final String TASKS = "/api/learning/tasks";
    public static final String TOPICS = "/api/learning/topics";
    public static final String TEST_CASES = "/api/learning/tests";
    public static final String CHANGE_TEST_CASE = "/api/learning/test";
    public static final String SOLUTIONS = "/api/solution";
    public static final String GET_USER_SOLUTIONS = "/api/solution/task";
    public static final String GET_TESTS_FOR_SERVICE = "/api/learning/service/tests";
    public static final String GET_TESTS = "/api/solution/tests";
    public static final String GET_TEST_INFO = "/api/solution/tests/info";
    public static final String USERNAME_CLAIM = "preferred_username";
    public static final String EMAIL_CLAIM = "email";
    public static final String GET_NOTIFICATIONS = "/api/notification";
    public static final String READ_NOTIFICATION = "/api/notification/read";
    public static final String READ_ALL_NOTIFICATIONS = "/api/notification/read/all";
    public static final String GET_UNREAD_NOTIFICATIONS = "/api/notification/unreaden";
    public static final String TOPIC = "notification-topic";
    public static final String MESSAGE_TOPIC = "message-topic";
    public static final String NOTIFICATION_GROUP = "notifications_group";
    public static final String SOLUTION_TOPIC = "solution-topic";
}
