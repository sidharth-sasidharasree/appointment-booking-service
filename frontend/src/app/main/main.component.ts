import{Component}from'@angular/core';

@Component({
selector: 'app-main',
standalone: false,
templateUrl: './main.component.html',
styleUrl: './main.component.scss'
})
export class MainComponent {

sideNavComponents = [{
"id": 1,
"label": "Appointments",
"route": "/main/appointments"
},{
"id": 2,
"label": "About",
"route": "/main/about"
}]

}
