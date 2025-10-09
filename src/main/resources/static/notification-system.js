// 通知系统API接口
class NotificationAPI {
    constructor() {
        this.baseUrl = '/api/notifications'; // 假设后端API地址
        this.notifications = this.loadFromStorage() || [];
    }

    // 从localStorage加载通知（模拟持久化）
    loadFromStorage() {
        try {
            const stored = localStorage.getItem('hospital_notifications');
            return stored ? JSON.parse(stored) : [];
        } catch (error) {
            console.error('加载通知数据失败:', error);
            return [];
        }
    }

    // 保存到localStorage（模拟持久化）
    saveToStorage() {
        try {
            localStorage.setItem('hospital_notifications', JSON.stringify(this.notifications));
        } catch (error) {
            console.error('保存通知数据失败:', error);
        }
    }

    // 发送通知
    async sendNotification(notificationData) {
        try {
            const notification = {
                id: `notification_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
                ...notificationData,
                timestamp: new Date(),
                isRead: false,
                createdAt: new Date().toISOString()
            };

            // 在实际项目中，这里应该是API调用
            // const response = await fetch(this.baseUrl, {
            //     method: 'POST',
            //     headers: {
            //         'Content-Type': 'application/json',
            //         'Authorization': `Bearer ${localStorage.getItem('token')}`
            //     },
            //     body: JSON.stringify(notification)
            // });

            // 模拟API调用成功
            this.notifications.unshift(notification);
            this.saveToStorage();
            
            console.log('通知已发送:', notification);
            return { success: true, data: notification };
        } catch (error) {
            console.error('发送通知失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 获取所有通知
    async getNotifications(filters = {}) {
        try {
            // 在实际项目中，这里应该是API调用
            // const response = await fetch(`${this.baseUrl}?${new URLSearchParams(filters)}`);
            
            let filteredNotifications = [...this.notifications];
            
            // 应用过滤器
            if (filters.module && filters.module !== 'all') {
                filteredNotifications = filteredNotifications.filter(n => n.module === filters.module);
            }
            
            if (filters.operation && filters.operation !== 'all') {
                filteredNotifications = filteredNotifications.filter(n => n.operation === filters.operation);
            }
            
            if (filters.status && filters.status !== 'all') {
                filteredNotifications = filteredNotifications.filter(n => 
                    filters.status === 'read' ? n.isRead : !n.isRead
                );
            }
            
            return { success: true, data: filteredNotifications };
        } catch (error) {
            console.error('获取通知失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 获取未读通知数量
    async getUnreadCount() {
        try {
            const unreadCount = this.notifications.filter(n => !n.isRead).length;
            return { success: true, data: unreadCount };
        } catch (error) {
            console.error('获取未读通知数量失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 标记通知为已读
    async markAsRead(notificationId) {
        try {
            const notification = this.notifications.find(n => n.id === notificationId);
            if (notification) {
                notification.isRead = true;
                notification.readAt = new Date().toISOString();
                this.saveToStorage();
            }
            return { success: true };
        } catch (error) {
            console.error('标记通知为已读失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 标记所有通知为已读
    async markAllAsRead() {
        try {
            this.notifications.forEach(n => {
                n.isRead = true;
                n.readAt = new Date().toISOString();
            });
            this.saveToStorage();
            return { success: true };
        } catch (error) {
            console.error('标记所有通知为已读失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 删除通知
    async deleteNotification(notificationId) {
        try {
            this.notifications = this.notifications.filter(n => n.id !== notificationId);
            this.saveToStorage();
            return { success: true };
        } catch (error) {
            console.error('删除通知失败:', error);
            return { success: false, error: error.message };
        }
    }

    // 清空所有通知
    async clearAllNotifications() {
        try {
            this.notifications = [];
            this.saveToStorage();
            return { success: true };
        } catch (error) {
            console.error('清空通知失败:', error);
            return { success: false, error: error.message };
        }
    }
}

// 通知助手类
class NotificationHelper {
    constructor() {
        this.api = new NotificationAPI();
    }

    // 发送病人管理通知
    async sendPatientNotification(operation, patientData, operatorName = '管理员') {
        const notificationData = {
            module: 'patient',
            operation: operation,
            title: this.getPatientNotificationTitle(operation),
            content: this.getPatientNotificationContent(operation, patientData),
            operatorName: operatorName,
            entityId: patientData.id || 'new',
            entityName: patientData.name || '未知病人',
            metadata: {
                patientId: patientData.id,
                patientName: patientData.name,
                patientPhone: patientData.phone
            }
        };

        return await this.api.sendNotification(notificationData);
    }

    // 发送医生管理通知
    async sendDoctorNotification(operation, doctorData, operatorName = '管理员') {
        const notificationData = {
            module: 'doctor',
            operation: operation,
            title: this.getDoctorNotificationTitle(operation),
            content: this.getDoctorNotificationContent(operation, doctorData),
            operatorName: operatorName,
            entityId: doctorData.id || 'new',
            entityName: doctorData.name || '未知医生',
            metadata: {
                doctorId: doctorData.id,
                doctorName: doctorData.name,
                department: doctorData.department,
                title: doctorData.title
            }
        };

        return await this.api.sendNotification(notificationData);
    }

    // 发送挂号管理通知
    async sendRegistrationNotification(operation, registrationData, operatorName = '管理员') {
        const notificationData = {
            module: 'registration',
            operation: operation,
            title: this.getRegistrationNotificationTitle(operation),
            content: this.getRegistrationNotificationContent(operation, registrationData),
            operatorName: operatorName,
            entityId: registrationData.id || 'new',
            entityName: `${registrationData.patientName || '未知病人'}的挂号`,
            metadata: {
                registrationId: registrationData.id,
                patientName: registrationData.patientName,
                doctorName: registrationData.doctorName,
                appointmentTime: registrationData.appointmentTime
            }
        };

        return await this.api.sendNotification(notificationData);
    }

    // 发送收费管理通知
    async sendPaymentNotification(operation, paymentData, operatorName = '管理员') {
        const notificationData = {
            module: 'payment',
            operation: operation,
            title: this.getPaymentNotificationTitle(operation),
            content: this.getPaymentNotificationContent(operation, paymentData),
            operatorName: operatorName,
            entityId: paymentData.id || 'new',
            entityName: `${paymentData.patientName || '未知病人'}的收费记录`,
            metadata: {
                paymentId: paymentData.id,
                patientName: paymentData.patientName,
                amount: paymentData.amount,
                paymentType: paymentData.paymentType
            }
        };

        return await this.api.sendNotification(notificationData);
    }

    // 发送药品管理通知
    async sendMedicineNotification(operation, medicineData, operatorName = '管理员') {
        const notificationData = {
            module: 'medicine',
            operation: operation,
            title: this.getMedicineNotificationTitle(operation),
            content: this.getMedicineNotificationContent(operation, medicineData),
            operatorName: operatorName,
            entityId: medicineData.id || 'new',
            entityName: medicineData.name || '未知药品',
            metadata: {
                medicineId: medicineData.id,
                medicineName: medicineData.name,
                code: medicineData.code,
                stock: medicineData.stock
            }
        };

        return await this.api.sendNotification(notificationData);
    }

    // 获取病人通知标题
    getPatientNotificationTitle(operation) {
        const titles = {
            CREATE: '新增病人信息',
            UPDATE: '更新病人信息',
            DELETE: '删除病人记录'
        };
        return titles[operation] || '病人信息变更';
    }

    // 获取病人通知内容
    getPatientNotificationContent(operation, data) {
        const contents = {
            CREATE: `新增病人"${data.name || '未知'}"的基本信息，请及时关注相关就诊安排。`,
            UPDATE: `病人"${data.name || '未知'}"的信息已更新，请确认相关医疗记录的准确性。`,
            DELETE: `病人"${data.name || '未知'}"的记录已被删除，相关挂号和收费记录也已同步删除。`
        };
        return contents[operation] || '病人信息发生变更，请及时关注。';
    }

    // 获取医生通知标题
    getDoctorNotificationTitle(operation) {
        const titles = {
            CREATE: '新增医生信息',
            UPDATE: '更新医生信息',
            DELETE: '删除医生记录'
        };
        return titles[operation] || '医生信息变更';
    }

    // 获取医生通知内容
    getDoctorNotificationContent(operation, data) {
        const contents = {
            CREATE: `新增医生"${data.name || '未知'}"(${data.department || '未知科室'})的信息，请协助完善相关排班安排。`,
            UPDATE: `医生"${data.name || '未知'}"的信息已更新，请确认科室安排和专业信息。`,
            DELETE: `医生"${data.name || '未知'}"的记录已删除，相关挂号记录已同步处理。`
        };
        return contents[operation] || '医生信息发生变更，请及时关注。';
    }

    // 获取挂号通知标题
    getRegistrationNotificationTitle(operation) {
        const titles = {
            CREATE: '新建挂号记录',
            UPDATE: '更新挂号信息',
            DELETE: '取消挂号记录'
        };
        return titles[operation] || '挂号信息变更';
    }

    // 获取挂号通知内容
    getRegistrationNotificationContent(operation, data) {
        const contents = {
            CREATE: `为病人"${data.patientName || '未知'}"新建挂号记录，请协助完成后续就诊流程。`,
            UPDATE: `病人"${data.patientName || '未知'}"的挂号信息已更新，请关注预约时间和科室变更。`,
            DELETE: `病人"${data.patientName || '未知'}"的挂号记录已取消，如有相关收费请及时处理退费。`
        };
        return contents[operation] || '挂号信息发生变更，请及时关注。';
    }

    // 获取收费通知标题
    getPaymentNotificationTitle(operation) {
        const titles = {
            CREATE: '新增收费记录',
            UPDATE: '更新收费信息',
            DELETE: '删除收费记录'
        };
        return titles[operation] || '收费信息变更';
    }

    // 获取收费通知内容
    getPaymentNotificationContent(operation, data) {
        const contents = {
            CREATE: `为病人"${data.patientName || '未知'}"新增收费记录，金额：￥${data.amount || '0'}，请确认收费项目和金额准确性。`,
            UPDATE: `病人"${data.patientName || '未知'}"的收费信息已更新，请核对相关收费详情。`,
            DELETE: `病人"${data.patientName || '未知'}"的收费记录已删除，如需退费请按相关流程处理。`
        };
        return contents[operation] || '收费信息发生变更，请及时关注。';
    }

    // 获取药品通知标题
    getMedicineNotificationTitle(operation) {
        const titles = {
            CREATE: '新增药品信息',
            UPDATE: '更新药品信息',
            DELETE: '删除药品记录'
        };
        return titles[operation] || '药品信息变更';
    }

    // 获取药品通知内容
    getMedicineNotificationContent(operation, data) {
        const contents = {
            CREATE: `新增药品"${data.name || '未知'}"(${data.code || ''})，库存：${data.stock || '0'}，请及时更新药房库存信息。`,
            UPDATE: `药品"${data.name || '未知'}"的信息已更新，请关注库存变动和价格调整。`,
            DELETE: `药品"${data.name || '未知'}"的记录已删除，请确认相关处方和库存状态。`
        };
        return contents[operation] || '药品信息发生变更，请及时关注。';
    }
}

// 导出全局实例
window.notificationHelper = new NotificationHelper();
window.notificationAPI = window.notificationHelper.api;

// 调试用的全局函数
window.debugNotifications = {
    // 查看所有通知
    viewAll: () => window.notificationAPI.getNotifications(),
    // 清空所有通知
    clear: () => window.notificationAPI.clearAllNotifications(),
    // 添加测试通知
    addTest: () => window.notificationHelper.sendPatientNotification('create', {name: '测试病人', id: 'test123'}),
    // 获取未读数量
    unreadCount: () => window.notificationAPI.getUnreadCount()
};

console.log('通知系统已初始化');
console.log('可用的调试函数:', Object.keys(window.debugNotifications));
