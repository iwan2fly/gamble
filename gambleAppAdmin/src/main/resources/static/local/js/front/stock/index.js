document.addEventListener('alpine:init', () => {
    Alpine.data('leftTab', () => ({
        selectedTab: 'info',

        tabChange: function( tab) {
            console.log( tab );
            this.selectedTab = tab;
        },
    }))
})

document.addEventListener('alpine:init', () => {
    Alpine.data('rightProcess', () => ({
        init() {
            console.log( "right process init");
        }
    }))
})



