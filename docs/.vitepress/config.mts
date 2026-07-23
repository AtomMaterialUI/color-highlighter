import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: 'Color Highlighter',
  description: 'Preview your colors inline in the JetBrains IDEs.',
  lang: 'en-US',
  lastUpdated: true,
  cleanUrls: true,

  head: [
    ['link', { rel: 'icon', type: 'image/svg+xml', href: 'https://raw.githubusercontent.com/mallowigi/color-highlighter/master/src/main/resources/META-INF/pluginIcon.svg?sanitize=true' }],
  ],

  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    logo: 'https://raw.githubusercontent.com/mallowigi/color-highlighter/master/src/main/resources/META-INF/pluginIcon.svg?sanitize=true',

    nav: [
      { text: 'Home', link: '/' },
      { text: 'Guide', link: '/guide/getting-started' },
      { text: 'Changelog', link: '/CHANGELOG' },
      {
        text: 'Marketplace',
        link: 'https://plugins.jetbrains.com/plugin/12320-color-highlighter',
      },
    ],

    sidebar: {
      '/guide/': [
        {
          text: 'Introduction',
          collapsed: false,
          items: [
            { text: 'Getting Started', link: '/guide/getting-started' },
            { text: 'Features', link: '/guide/features' },
          ],
        },
        {
          text: 'Usage',
          collapsed: false,
          items: [
            { text: 'Supported Languages', link: '/guide/supported-languages' },
            { text: 'Color Formats', link: '/guide/color-formats' },
            { text: 'Configuration', link: '/guide/configuration' },
            { text: 'Gutter & Copying', link: '/guide/gutter' },
            { text: 'Custom Colors', link: '/guide/custom-colors' },
          ],
        },
        {
          text: 'Help',
          collapsed: false,
          items: [
            { text: 'Gotchas', link: '/guide/gotchas' },
            { text: 'Troubleshooting', link: '/guide/troubleshooting' },
            { text: 'FAQ', link: '/guide/faq' },
          ],
        },
      ],
    },

    socialLinks: [
      { icon: 'github', link: 'https://github.com/mallowigi/color-highlighter' },
    ],

    editLink: {
      pattern:
        'https://github.com/mallowigi/color-highlighter/edit/master/docs/:path',
      text: 'Edit this page on GitHub',
    },

    search: {
      provider: 'local',
    },

    footer: {
      message: 'Released under the MIT License.',
      copyright: 'Copyright © 2015-present Elior "Mallowigi" Boukhobza',
    },
  },
})
