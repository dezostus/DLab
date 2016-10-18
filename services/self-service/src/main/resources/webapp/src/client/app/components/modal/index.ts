import {Modal} from "./modal.component";
//import {RouteModal} from "./modal.routes";
import {CommonModule} from "@angular/common";
import {NgModule, Component} from "@angular/core";

export * from "./modal.component";
//export * from "./modal.routes";

@Component({
    selector: "modal-header",
    template: `<ng-content></ng-content>`
})
export class ModalHeader {
}

@Component({
    selector: "modal-content",
    template: `<ng-content></ng-content>`
})
export class ModalContent {
}

@Component({
    selector: "modal-footer",
    template: `<ng-content></ng-content>`
})
export class ModalFooter {
}

@NgModule({
    imports: [
        CommonModule
    ],
    declarations: [
        Modal,
        //RouteModal,
        ModalHeader,
        ModalContent,
        ModalFooter,
    ],
    exports: [
        Modal,
        //RouteModal,
        ModalHeader,
        ModalContent,
        ModalFooter,
    ],
})
export class ModalModule {

}
