    <%@ page import="model.User" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <c:set var="user" value="${sessionScope.LOGGED_USER}" />
    <c:set var="address" value="${user.address}" />
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>HOME DECOR - CÀI ĐẶT</title>
        <link rel="icon" type="image/png"  href="img/logo.png" >

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="css/admin_seting_style.css">

    </head>
    <body>
    <div class="admin-container">
        <%@ include file="admin_header.jsp" %>
        <div class="main-wrapper">
            <%@ include file="admin_sidebar.jsp" %>

            <main class="main-content">
                <div class="settings-layout">
                    <div class="settings-menu">
                        <ul>
                            <li><a href="#basic-info">Thông tin cơ bản</a></li>
                            <li><a href="#password">Mật khẩu</a></li>
                            <li><a href="#notification">Thông báo</a></li>
                            <li><a href="#connection">Tài khoản & Kết nối</a></li>
                            <li><a href="#delete-account">Xóa tài khoản</a></li>

                        </ul>
                    </div>

                    <div class="settings-content">

                        <div class="settings-card" id="basic-info">
                            <h2>Thông tin cơ bản</h2>
                            <form action="${pageContext.request.contextPath}/UpdateSettingServlet" method="post" class="basic-info-form">

                                <!-- Avatar -->
                                <c:choose>
                                    <c:when test="${not empty user.avatarUrl}">
                                        <img src="${user.avatarUrl}" alt="Avatar" class="profile-avatar">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="img/default-avatar.png" alt="Default Avatar" class="profile-avatar">
                                    </c:otherwise>
                                </c:choose>

                                <!-- Họ tên & Tên hiển thị -->
                                <div class="form-row">
                                    <div class="form-group half-width">
                                        <label>Họ và tên</label>
                                        <input type="text" name="full_name" value="${user.username}">
                                    </div>
                                    <div class="form-group half-width">
                                        <label>Tên hiển thị</label>
                                        <input type="text" name="display_name" value="${user.displayName}">
                                    </div>
                                </div>

                                <!-- Email & SĐT -->
                                <div class="form-row">
                                    <div class="form-group half-width">
                                        <label>Email</label>
                                        <input type="email" value="${user.email}" readonly>
                                    </div>
                                    <div class="form-group half-width">
                                        <label>Số điện thoại</label>
                                        <input type="text" name="phone" value="${user.phone}">
                                    </div>
                                </div>

                                <!-- Giới tính & Ngày sinh -->
                                <div class="form-row">
                                    <div class="form-group half-width">
                                        <label>Giới tính</label>
                                        <select name="gender">
                                            <option value="">-- Chọn --</option>
                                            <option value="Male"   ${user.gender == 'Male' ? 'selected' : ''}>Nam</option>
                                            <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                                            <option value="Other"  ${user.gender == 'Other' ? 'selected' : ''}>Khác</option>
                                        </select>
                                    </div>
                                    <div class="form-group half-width">
                                        <label>Ngày sinh</label>
                                        <fmt:formatDate value="${user.birthDate}" pattern="yyyy-MM-dd" var="birth"/>
                                        <input type="date" name="birth_date" value="${birth}">
                                    </div>
                                </div>

                                <!-- ------------------- ĐỊA CHỈ ------------------- -->
                                <h3>Địa chỉ</h3>
                                <div class="form-row">
                                    <div class="form-group full-width">
                                        <label>Địa chỉ chi tiết</label>
                                        <input type="text" name="detail"
                                               value="${address != null ? address.detail : ''}"
                                               placeholder="Địa chỉ chi tiết" />
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group third-width">
                                        <label>Phường / Xã</label>
                                        <input type="text" name="commune"
                                               value="${address != null ? address.commune : ''}"
                                               placeholder="Phường / Xã" />
                                    </div>
                                    <div class="form-group third-width">
                                        <label>Quận / Huyện</label>
                                        <input type="text" name="district"
                                               value="${address != null ? address.district : ''}"
                                               placeholder="Quận / Huyện" />
                                    </div>
                                    <div class="form-group third-width">
                                        <label>Tỉnh / Thành phố</label>
                                        <input type="text" name="province"
                                               value="${address != null ? address.province : ''}"
                                               placeholder="Tỉnh / Thành phố" />
                                    </div>
                                </div>

                                <!-- Nút Lưu -->
                                <div class="form-actions">
                                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                                </div>

                            </form>
                        </div>



                        <div class="settings-card" id="notification">
                            <h2>Thông báo</h2>

                            <div class="alert-box">
                                Chúng tôi cần quyền từ trình duyệt của bạn để hiển thị thông báo. <a href="#">**Yêu cầu quyền**</a>
                            </div>

                            <div class="notification-table">
                                <div class="notification-header">
                                    <div class="col-type">Loại</div>
                                    <div class="col-email">Email</div>
                                    <div class="col-browser">Trình duyệt</div>
                                    <div class="col-app">Ứng dụng</div>
                                </div>

                                <div class="notification-row">
                                    <div class="col-type">Mời dành cho bạn</div>
                                    <div class="col-email"><input type="checkbox"></div>
                                    <div class="col-browser"><input type="checkbox"></div>
                                    <div class="col-app"><input type="checkbox"></div>
                                </div>

                                <div class="notification-row">
                                    <div class="col-type">Hoạt động tài khoản</div>
                                    <div class="col-email"><input type="checkbox"></div>
                                    <div class="col-browser"><input type="checkbox"></div>
                                    <div class="col-app"><input type="checkbox"></div>
                                </div>

                                <div class="notification-row">
                                    <div class="col-type">Trình duyệt mới được sử dụng để đăng nhập</div>
                                    <div class="col-email"><input type="checkbox" checked></div>
                                    <div class="col-browser"><input type="checkbox" checked></div>
                                    <div class="col-app"><input type="checkbox" checked></div>
                                </div>
                            </div>

                            <div class="schedule-select">
                                <label for="schedule">Khi nào chúng tôi nên gửi thông báo cho bạn?</label>
                                <select id="schedule">
                                    <option value="always">Luôn luôn</option>
                                    <option value="daily">Hàng ngày</option>
                                    <option value="weekly">Hàng tuần</option>
                                </select>
                                <button class="btn btn-primary">Lưu thay đổi</button>
                            </div>
                        </div>



                        <div class="settings-card" id="connection">
                            <h2>Tài khoản và Kết nối</h2>
                            <p class="section-description">Quản lý các dịch vụ bên thứ ba được phép truy cập dữ liệu tài khoản của bạn.</p>

                            <div class="connection-section">
                                <h3>Tài khoản tích hợp</h3>
                                <div class="connection-list">
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-google"></i>
                                            <div>
                                                <h4>Google</h4>
                                                <p>Lịch và danh bạ</p>
                                            </div>
                                        </div>
                                        <label class="switch">
                                            <input type="checkbox">
                                            <span class="slider round"></span>
                                        </label>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fas fa-tasks"></i>
                                            <div>
                                                <h4>Spec</h4>
                                                <p>Quản lý dự án</p>
                                            </div>
                                        </div>
                                        <label class="switch">
                                            <input type="checkbox">
                                            <span class="slider round"></span>
                                        </label>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-slack"></i>
                                            <div>
                                                <h4>Slack</h4>
                                                <p>Liên lạc</p>
                                            </div>
                                        </div>
                                        <label class="switch">
                                            <input type="checkbox">
                                            <span class="slider round"></span>
                                        </label>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fas fa-envelope-open-text"></i>
                                            <div>
                                                <h4>Mailchimp</h4>
                                                <p>Dịch vụ tiếp thị email</p>
                                            </div>
                                        </div>
                                        <label class="switch">
                                            <input type="checkbox">
                                            <span class="slider round"></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="social-media-section">
                                <h3>Tài khoản mạng xã hội </h3>
                                <p class="section-description">Kết nối các tài khoản này giúp bạn dễ dàng kết nối với bạn bè và chia sẻ cập nhật.</p>
                                <div class="connection-list">
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-twitter"></i>
                                            <div>
                                                <h4>Twitter</h4>
                                                <p class="status-disconnected">Chưa kết nối</p>
                                            </div>
                                        </div>
                                        <button class="btn btn-secondary">Kết nối</button>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-facebook"></i>
                                            <div>
                                                <h4>Facebook</h4>
                                                <p class="status-disconnected">Chưa kết nối</p>
                                            </div>
                                        </div>
                                        <button class="btn btn-secondary">Kết nối</button>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-linkedin-in"></i>
                                            <div>
                                                <h4>LinkedIn</h4>
                                                <p class="status-disconnected">Chưa kết nối</p>
                                            </div>
                                        </div>
                                        <button class="btn btn-secondary">Kết nối</button>
                                    </div>
                                    <div class="connection-item">
                                        <div class="connection-details">
                                            <i class="fab fa-google"></i>
                                            <div>
                                                <h4>Google</h4>
                                                <p class="status-disconnected">Chưa kết nối</p>
                                            </div>
                                        </div>
                                        <button class="btn btn-secondary">Kết nối</button>
                                    </div>
                                </div>
                            </div>
                        </div>

    <!--                    <div class="settings-card" id="connection2">-->
    <!--                        <h2>Xóa tài khoản</h2>-->

                        <div class="settings-card" id="delete-account">
                            <h2>Xóa tài khoản</h2>
                            <p class="section-description">
                                Hành động này sẽ xóa vĩnh viễn tất cả dữ liệu tài khoản, bao gồm hồ sơ, cài đặt, và lịch sử hoạt động. Không thể hoàn tác.
                            </p>

                            <form action="${pageContext.request.contextPath}/DeleteAccountServlet"
                                  method="post"
                                  onsubmit="return confirm('Bạn có chắc chắn muốn xóa tài khoản? Hành động này KHÔNG thể hoàn tác!');">

                                <button type="submit" class="btn btn-danger">
                                    Xóa tài khoản
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    </div>
    <script src="js/admin_setting.js"></script>
    </body>

    </html>