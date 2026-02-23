const PHONE_REGEX =
  /^((\\+[1-9]{1,4}[ \\-]*)|(\\([0-9]{2,3}\\)[ \\-]*)|([0-9]{2,4})[ \\-]*)*?[0-9]{3,4}?[ \\-]*[0-9]{3,4}?$/;

const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

const PASSWORD_RULES = {
  hasLowercase: /[a-z]/,
  hasUppercase: /[A-Z]/,
  allowedChars: /^[A-Za-z0-9!@#$%^&*()_+\-=[\]{}|,.?]+$/
};

const INVALID_PASSWORD_MESSAGES = {
  required: "Mật khẩu không được để trống.",
  invalidFormat: "Mật khẩu không hợp lệ."
};

const INVALID_USERNAME_MESSAGES = {
  required: "Tên đăng nhập không được để trống.",
  invalidFormat: "Tên đăng nhập phải là email hoặc số điện thoại."
};

export {
  PHONE_REGEX,
  EMAIL_REGEX,
  PASSWORD_RULES,
  INVALID_PASSWORD_MESSAGES,
  INVALID_USERNAME_MESSAGES
};
