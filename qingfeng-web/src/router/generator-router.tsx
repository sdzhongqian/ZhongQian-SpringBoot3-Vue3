import { Result } from 'ant-design-vue';
import { notFound, errorRoute } from './staticModules/error';
import { REDIRECT_ROUTE } from './staticModules/besidesLayout';
import outsideLayout from './outsideLayout';
import type { PermissionType } from '@/core/permission/modules/types';
import type { RouteRecordRaw } from 'vue-router';
import RouterView from '@/layout/routerView/index.vue';
import { isUrl } from '@/utils/is';
import { uniqueSlash } from '@/utils/urlUtils';
import { asyncRoutes } from '@/router/asyncModules';
import common from '@/router/staticModules';
import router, { routes } from '@/router';
import NotFound from '@/views/error/404.vue';
import IFramePage from '@/components/basic/iframe-page';

// 需要放在所有路由之后的路由
const endRoutes: RouteRecordRaw[] = [REDIRECT_ROUTE, errorRoute, notFound];


function filterAsyncRouter (routerMap, roles) {
  return routerMap.filter(route => {
    if (hasPermission(roles.permissionList, route)) {
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, roles)
      }
      return true
    }
    return false
  })
}

/**
 * 过滤账户是否拥有某一个权限，并将菜单从加载列表移除
 *
 * @param permission
 * @param route
 * @returns {boolean}
 */
function hasPermission (permission, route) {
  if (route.meta && route.meta.permission) {
    let flag = false
    for (let i = 0, len = permission.length; i < len; i++) {
      flag = route.meta.permission.includes(permission[i])
      if (flag) {
        return true
      }
    }
    return false
  }else if(route.meta.permission==undefined){
    return false
  }
  return true
}



/**
 * 动态生成菜单
 * @param token
 * @returns {Promise<Router>}
 */
export const generatorDynamicRouter = (asyncMenus: API.Menu[],roles) => {
  try {
    // console.log('asyncMenus', asyncMenus);
    const treeData = fommat({
      arrayList: asyncMenus,
      pidStr: "parent_id",
    });
    const routeList = filterAsyncRouter(treeData, roles)
    const layout = routes.find((item) => item.name == 'Layout')!;
    console.log(routeList, '根据后端返回的权限路由生成');
    // 给公共路由添加namePath
    generatorNamePath(common);
    generatorNamePath(routeList);
    const menus = [...routeList, ...common,  ...endRoutes];
    layout.children = menus;
    const removeRoute = router.addRoute(layout);
    // 获取所有没有包含children的路由，上面addRoute的时候，vue-router已经帮我们拍平了所有路由
    const filterRoutes = router
      .getRoutes()
      .filter(
        (item) =>
          (!item.children.length || Object.is(item.meta?.hideChildrenInMenu, true)) &&
          !outsideLayout.some((n) => n.name === item.name),
      );
    // 清空所有路由
    removeRoute();
    layout.children = [...filterRoutes];
    // 重新添加拍平后的路由
    router.addRoute(layout);
    console.log('所有路由', router.getRoutes());

    return Promise.resolve({
      menus,
      routes: layout.children,
    });
  } catch (error) {
    console.error('生成路由时出错', error);
    return Promise.reject(`生成路由时出错: ${error}`);
  }
};

/**
 * 主要方便于控制a-menu的open-keys，即控制左侧菜单应当展开哪些菜单
 * @param {RouteRecordRaw[]} routes 需要添加namePath的路由
 * @param {string[]} namePath
 */
export const generatorNamePath = (
  routes: RouteRecordRaw[],
  namePath?: string[],
  parent?: RouteRecordRaw,
) => {
  routes.forEach((item) => {
    if (item.meta && typeof item.name === 'string') {
      item.meta.namePath = Array.isArray(namePath) ? namePath.concat(item.name) : [item.name];
      item.meta.fullPath = parent?.meta?.fullPath
        ? [parent.meta.fullPath, item.path].join('/')
        : item.path;
      item.meta.fullPath = uniqueSlash(item.meta.fullPath);

      if (item.children?.length) {
        generatorNamePath(item.children, item.meta.namePath, item);
      }
    }
  });
};



function filterEmptyDirectory(routerMap) {
  const accessedRouters = routerMap.filter(route => {
    if (route.children && route.children.length) {
      route.children = filterEmptyDirectory(route.children)
      return true
    } else {
      // if (route.meta.permission.includes('system')) {
      //   return false
      // } else {
      //   return true
      // }
      return true
    }
  })
  return accessedRouters
}


function fommat({
  arrayList,
  pidStr = "parent_id",
  idStr = "id",
  childrenStr = "children",
}) {
  let listOjb = {}; // 用来储存{key: obj}格式的对象
  let treeList = []; // 用来储存最终树形结构数据的数组
  // 将数据变换成{key: obj}格式，方便下面处理数据
  for (let i = 0; i < arrayList.length; i++) {
    var data = arrayList[i];
    // if(data.path.indexOf('?')!=-1){
    //   data.param = data.path.substring(data.path.indexOf('?'));
    //   data.path = data.path.substring(0,data.path.indexOf('?'));
    // }

    data.key = data.id;
    //处理菜单格式信息
    if (data.type == '0') {//目录
      data.component = RouterView
    } else if (data.type == '1') {
      const views = data.component;
      if (views == '/dashboard/welcome' || views == '/dashboard/Welcome') {
        data.component = () => import(`../views/dashboard/welcome/index.vue`)
      } else {
        let vurl = `../views${views}.vue`;
        data.component = modules[vurl] as any;
        // data.component = () => import(`@/views${views}/Index.vue`)
        // data.component = (resolve) => require([`@/views${views}/Index`], resolve)
      }
      // data.component = () => import(`@${views}`)
      // data.component = () => import('@/views${component}')
    } else if (data.type == '4') {
      data.component = <IFramePage src={data.path} />
    }
    let role_ids = data.role_ids?.split(',')
    if(data.path=='/'&&role_ids==undefined){
      role_ids = ['system']
    }

    let keepalive = JSON.parse(data.keepAlive);
    if (data.type == '3') {
      data.name = data.path;
      data.meta = {
        title: data.title,
        keepalive: keepalive,
        icon: data.icon,
        permission: role_ids,
        isExt: true,
        openMode: 1
      }
    } else {
      data.meta = {
        title: data.title,
        keepalive: keepalive,
        icon: data.icon,
        permission: role_ids,
        isExt: false
      }
    }

    // console.log(data.meta)
    listOjb[arrayList[i][idStr]] = data;
  }
  // 根据pid来将数据进行格式化
  for (let j = 0; j < arrayList.length; j++) {
    // 判断父级是否存在
    let haveParent = listOjb[arrayList[j][pidStr]];
    if (haveParent) {
      // 如果有没有父级children字段，就创建一个children字段
      !haveParent[childrenStr] && (haveParent[childrenStr] = []);
      // 在父级里插入子项
      haveParent[childrenStr].push(arrayList[j]);
    } else {
      // 如果没有父级直接插入到最外层
      treeList.push(arrayList[j]);
    }
  }
  return treeList;
}

const modules = import.meta.glob("../views/**/**.vue")